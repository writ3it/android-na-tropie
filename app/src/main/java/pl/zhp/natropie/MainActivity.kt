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
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import android.view.Menu
import android.view.MenuItem
import org.parceler.Parcels
import pl.zhp.natropie.db.DBWorkerThread
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.ui.ContentService
import pl.zhp.natropie.ui.PostLists.PostsAdapter
import pl.zhp.natropie.ui.PostLists.PostsListPresenter
import pl.zhp.natropie.ui.models.PostVM

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var db: NaTropieDB? = null

    private lateinit var thread: DBWorkerThread

    private lateinit var postPresenter: PostsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()
        setMenu(nav_view.menu)
        nav_view.setNavigationItemSelectedListener(this)
        initPostList()
    }

    private lateinit var postsAdapter: PostsAdapter

    private fun initPostList() {
        postsAdapter = PostsAdapter(applicationContext, emptyList<PostVM>().toMutableList())
        postsListView.adapter = postsAdapter
        postPresenter = PostsListPresenter(applicationContext, NaTropieDB.getInstance(applicationContext)?.postsRepository()!!)
        postPresenter.attachToAdapter(postsAdapter)

        val respondend = false;

        ContentService.listenGetPosts(applicationContext,
            fun(context: Context?, intent: Intent?):Unit {
                postPresenter.setCategoryId(selectedCategoryId)
                postPresenter.refresh()
                pullToRefresh.isRefreshing = false
            })
        ContentService.getPosts(applicationContext)

        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = true
            ContentService.getPosts(applicationContext)

        }
    }

    override fun onDestroy() {
        NaTropieDB.destroyInstance()
        postPresenter.detachView()
        thread.quit()
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
        ContentService.listenGetMenu( applicationContext,
            fun(context: Context?, intent: Intent?):Unit {
                val categories = intent!!.getParcelableArrayExtra(ContentService.RESPONSE_VAR_MENU).map{
                    Parcels.unwrap<Category>(it)
                }
                var i = 0
                for(category in categories){
                    menu?.add(0, Menu.FIRST+category.id, Menu.NONE, category.name)
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


    private var selectedCategoryId: Int = 0

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectedCategoryId = item.itemId - Menu.FIRST
        ContentService.getPosts(applicationContext,selectedCategoryId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
