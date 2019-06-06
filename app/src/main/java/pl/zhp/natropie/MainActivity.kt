package pl.zhp.natropie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.content_main.*
import org.parceler.Parcels
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.services.ContentService
import pl.zhp.natropie.services.PushNotificationService
import pl.zhp.natropie.ui.PostLists.PostsAdapter
import pl.zhp.natropie.ui.PostLists.PostsListPresenter
import pl.zhp.natropie.ui.models.PostVM

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener
   {
       /**
        * Callback method to be invoked when an item in this AdapterView has
        * been clicked.
        *
        *
        * Implementers can call getItemAtPosition(position) if they need
        * to access the data associated with the selected item.
        *
        * @param parent The AdapterView where the click happened.
        * @param view The view within the AdapterView that was clicked (this
        * will be a view provided by the adapter)
        * @param position The position of the view in the adapter.
        * @param id The row id of the item that was clicked.
        */
   override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val post = postsAdapter.getItem(position) ?: return
        val intent = Intent(this,ReaderActivity::class.java).apply{
            putExtra(ReaderActivity.VAR_POST, Parcels.wrap(post.Model))
        }
        startActivity(intent)
   }

    private lateinit var db: NaTropieDB

    private lateinit var postPresenter: PostsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Track.initializeWithContext(applicationContext)
        super.onCreate(savedInstanceState)
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
    }

    private lateinit var postsAdapter: PostsAdapter


    private fun initPostList(savedInstanceState:Bundle?) {
        postsAdapter = PostsAdapter(applicationContext, emptyList<PostVM>().toMutableList())
        postsListView.adapter = postsAdapter
        postsListView.onItemClickListener = this
        postPresenter = PostsListPresenter(applicationContext, NaTropieDB.getInstance(applicationContext)?.postsRepository()!!)
        postPresenter.attachToAdapter(postsAdapter)

        val respondend = false;

        ContentService.listenGetPosts(applicationContext,
            fun(context: Context?, intent: Intent?):Unit {
                postPresenter.setCategoryId(selectedCategoryId)
                postPresenter.refresh()
                pullToRefresh.isRefreshing = false
            })
        if (savedInstanceState == null) {
            ContentService.getPostsWithDelay(applicationContext)
        } else {
            selectedCategoryId = savedInstanceState.getInt(VAR_CATEGORY_ID)
            ContentService.getPostsWithDelay(applicationContext,selectedCategoryId)
        }

        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = true
            ContentService.getPosts(applicationContext)

        }
    }

    override fun onDestroy() {
        NaTropieDB.destroyInstance()
        postPresenter.detachView()
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
        menu?.add(0,Menu.FIRST,Menu.NONE,getString(R.string.main_menu_item_name))
        ContentService.listenGetMenu( applicationContext,
            fun(context: Context?, intent: Intent?):Unit {
                val categories = intent!!.getParcelableArrayExtra(ContentService.RESPONSE_VAR_MENU).map{
                    Parcels.unwrap<Category>(it)
                }
                var i = 0
                for(category in categories){
                    menu?.add(0, Menu.FIRST+category.id.toInt(), Menu.NONE, category.name)
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
           outState?.putInt(VAR_CATEGORY_ID,selectedCategoryId)
           super.onSaveInstanceState(outState)
       }

    private var selectedCategoryId: Int = 0

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectedCategoryId = item.itemId - Menu.FIRST
        ContentService.getPosts(applicationContext,selectedCategoryId)
        postsListView.setSelectionAfterHeaderView();
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        const val VAR_CATEGORY_ID = "pl.zhp.natropie.MainActivity.VAR_CATEGORY_ID";
    }
}
