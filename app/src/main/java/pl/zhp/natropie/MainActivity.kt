package pl.zhp.natropie

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.content_main.*
import org.parceler.Parcels
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.dialogs.PrivacyActivity
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.services.ContentService
import pl.zhp.natropie.services.PushNotificationService
import pl.zhp.natropie.ui.PostLists.PostsAdapter
import pl.zhp.natropie.ui.PostLists.PostsListPresenter
import pl.zhp.natropie.ui.models.PostVM

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val post = postsAdapter.getItem(position) ?: return
        openReader<ReaderActivity>(post)
    }

    private inline fun <reified T : Any> openReader(post: PostVM) {
        val intent = Intent(this, T::class.java).apply {
            putExtra(ReaderActivity.VAR_POST, Parcels.wrap(post.Model))
        }
        startActivity(intent)
    }

    private lateinit var db: NaTropieDB

    private lateinit var postPresenter: PostsListPresenter

    private var queringAgreement:Boolean = false
    private var queringAgreementStarts:Boolean = false
    private var initialized:Boolean = false

    override fun onPause() {
        super.onPause()
        queringAgreementStarts = queringAgreement
    }

    override fun onResume() {
        super.onResume()
        if (queringAgreement && queringAgreementStarts){
            queringAgreementStarts = false
            queringAgreement = false
            if (!privacyCheck(false)){
                finish()
            }
            if (!initialized){
                initializeMain(null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContentService.listenEnsurePrivacyPolicy(applicationContext,
            fun(context: Context?, intent: Intent?): Unit {
                val post = intent!!.getParcelableArrayExtra(ContentService.RESPONSE_ENSURE_PRIVACY_POLICY).map {
                    Parcels.unwrap<PostWithColor>(it)
                }
                val postVM = PostVM(post.first()!!)
                Log.e(">>>>>>>","opened Privacy")
                openReader<PrivacyActivity>(postVM)
            })
        queringAgreement= true
        if (privacyCheck(true)) {
            queringAgreement = false
            initializeMain(savedInstanceState)
        }
    }

    private fun initializeMain(savedInstanceState: Bundle?) {
        Track.initializeWithContext(applicationContext)
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.notification_topic))
            .addOnCompleteListener { task ->
                Log.d(PushNotificationService.TAG, task.isSuccessful.toString())
            }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setMenu(nav_view.menu)
        nav_view.setNavigationItemSelectedListener(this)
        initPostList(savedInstanceState)
        initialized = true
    }

    private fun privacyCheck(queryAgreement:Boolean):Boolean {
        val config = getSharedPreferences(PrivacyActivity.SHARED_NAME, IntentService.MODE_PRIVATE)
        if (!config.getBoolean(PrivacyActivity.STATE_NAME, false)) {
            if (queryAgreement) {
                ContentService.ensurePrivacyPolicy(applicationContext)
            }
            return false
        }
        return true
    }

    private lateinit var postsAdapter: PostsAdapter


    private fun initPostList(savedInstanceState: Bundle?) {
        postsAdapter = PostsAdapter(applicationContext, emptyList<PostVM>().toMutableList())
        postsListView.adapter = postsAdapter
        postsListView.onItemClickListener = this
        postPresenter =
            PostsListPresenter(applicationContext, NaTropieDB.getInstance(applicationContext)?.postsRepository()!!)
        postPresenter.attachToAdapter(postsAdapter)

        val respondend = false

        ContentService.listenGetPosts(applicationContext,
            fun(context: Context?, intent: Intent?): Unit {
                postPresenter.setCategoryId(selectedCategoryId)
                postPresenter.refresh()
                pullToRefresh.isRefreshing = false
            })
        if (savedInstanceState == null) {
            ContentService.getPostsWithDelay(applicationContext)
        } else {
            selectedCategoryId = savedInstanceState.getInt(VAR_CATEGORY_ID)
            ContentService.getPostsWithDelay(applicationContext, selectedCategoryId)
        }

        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = true
            ContentService.getPosts(applicationContext)
        }

        ContentService.listenEnsureAboutUs(applicationContext,
            fun(context: Context?, intent: Intent?): Unit {
                val post = intent!!.getParcelableArrayExtra(ContentService.RESPONSE_ENSURE_ABOUT_US).map {
                    Parcels.unwrap<PostWithColor>(it)
                }
                val postVM = PostVM(post.first()!!)
                openReader<ReaderActivity>(postVM)
            })

    }

    override fun onDestroy() {
        if (initialized) {
            NaTropieDB.destroyInstance()
            postPresenter.detachView()
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun setMenu(menu: Menu?) {
        menu?.add(0, Menu.FIRST, Menu.NONE, getString(R.string.main_menu_item_name))
        ContentService.listenGetMenu(applicationContext,
            fun(context: Context?, intent: Intent?): Unit {
                val categories = intent!!.getParcelableArrayExtra(ContentService.RESPONSE_VAR_MENU).map {
                    Parcels.unwrap<Category>(it)
                }
                var i = 0
                for (category in categories) {
                    menu?.add(0, Menu.FIRST + category.id.toInt(), Menu.NONE, category.name)
                    i++
                }
            }

        )
        ContentService.startGetMenu(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(VAR_CATEGORY_ID, selectedCategoryId)
        super.onSaveInstanceState(outState)
    }

    private var selectedCategoryId: Int = 0

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId > LIMIT_ITEM_ID) {
            customMenuItemsDispatch(item)
        } else {
            selectedCategoryId = item.itemId - Menu.FIRST
            ContentService.getPosts(applicationContext, selectedCategoryId)
            postsListView.setSelectionAfterHeaderView()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun customMenuItemsDispatch(item: MenuItem) {
        when (item.itemId) {
            R.id.about_us_menu_item -> aboutUsMenuItemOnClick()
            R.id.privacy_policy -> openPrivacyPolicy()
        }
    }

    private fun openPrivacyPolicy() {
        queringAgreement= true
        ContentService.ensurePrivacyPolicy(applicationContext)
        //val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.PRIVACY_POLICY_URL)))
        //startActivity(browserIntent)
    }

    private fun aboutUsMenuItemOnClick() {
        ContentService.ensureAboutUs(applicationContext)
    }


    companion object {
        const val VAR_CATEGORY_ID = "pl.zhp.natropie.MainActivity.VAR_CATEGORY_ID";
        const val LIMIT_ITEM_ID = 1000;
    }
}
