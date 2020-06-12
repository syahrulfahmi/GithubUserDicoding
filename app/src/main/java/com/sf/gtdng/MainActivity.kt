package com.sf.gtdng

import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sf.gtdng.adapter.GithubUserAdapter
import com.sf.gtdng.adapter.GithubUserResultAdapter
import com.sf.gtdng.db.DatabaseContract
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.sf.gtdng.db.GithubUserFavoriteHelper
import com.sf.gtdng.fragment.DialogNoConnection
import com.sf.gtdng.helper.Extra
import com.sf.gtdng.helper.MappingHelper
import com.sf.gtdng.helper.hideKeyboard
import com.sf.gtdng.helper.inputStreamToString
import com.sf.gtdng.model.GithubUserModel
import com.sf.gtdng.network.NetworkChecking
import com.sf.gtdng.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var githubUserResultAdapter: GithubUserResultAdapter
    private lateinit var viewModel: GithubUserViewModel
    private lateinit var githubUserFavoriteHelper: GithubUserFavoriteHelper

    private var isChange = false
    private var isFromFavorite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        githubUserFavoriteHelper = GithubUserFavoriteHelper.getInstance(this)
        githubUserFavoriteHelper.open()

        loadData()
        initObserver()
        initRecyclerView()
        initListener()
        initContentProvider()
    }

    private fun initContentProvider () {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadData()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivityForResult(intent, Extra.FAVORITE)
            }
            R.id.setting -> {
                Intent(this@MainActivity, ReminderSettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Extra.FAVORITE) {
            isFromFavorite = false
            loadData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            maxWidth = Int.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)
            setQuery(viewModel.param, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                // call on every text change
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!isChange) {
                        if (newText.equals("")) {
                            this.onQueryTextSubmit("")
                        }
                    }
                    return true
                }

                // call if only press the submit button
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //check connection is available or not
                    if (!NetworkChecking.isNetworkAvailable(this@MainActivity)) {
                        val dialog = DialogNoConnection()
                        dialog.onButtonClickListener = {
                            if (NetworkChecking.isNetworkAvailable(this@MainActivity)) {
                                dialog.dismiss()
                                searchView.setQuery(viewModel.param, true)
                            }
                        }
                        dialog.onBackPressed = { finish() }
                        dialog.show(supportFragmentManager, DialogNoConnection.TAG)
                    }
                    rvGithubUserResult.visibility = View.GONE
                    textInfo.visibility = View.GONE
                    if (query.equals("")) {
                        viewModel.param = query
                        rvGithubUser.visibility = View.VISIBLE
                        progressLoading.visibility = View.GONE
                    } else {
                        rvGithubUser.visibility = View.GONE
                        viewModel.param = query
                        viewModel.getData()
                        hideKeyboard(this@MainActivity, searchView)
                        progressLoading.visibility = View.VISIBLE
                    }
                    return true
                }
            })
        }

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (viewModel.param.isNullOrEmpty()) {
                    rvGithubUser.visibility = View.VISIBLE
                    rvGithubUserResult.visibility = View.GONE
                    textInfo.visibility = View.GONE
                } else {
                    if (githubUserResultAdapter.items.isEmpty()) {
                        rvGithubUser.visibility = View.VISIBLE
                    }
                    rvGithubUserResult.visibility = View.VISIBLE
                    textInfo.visibility = View.GONE
                }
                isChange = true
                return true
            }

            //retreive query when searcview is expanded again
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searchView.onActionViewExpanded()
                searchView.setQuery(viewModel.param, false)
                isChange = false
                return true
            }
        })
        return true

    }

    private fun initObserver() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GithubUserViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            progressLoading.visibility = View.GONE
            if (it.isNotEmpty()) {
                rvGithubUser.visibility = View.GONE
                rvGithubUserResult.visibility = View.VISIBLE
                githubUserResultAdapter.removeAll()
                githubUserResultAdapter.addAll(it)
            } else {
                textInfo.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecyclerView() {
        githubUserAdapter = GithubUserAdapter(this)
        githubUserResultAdapter = GithubUserResultAdapter(this)
        rvGithubUser.adapter = githubUserAdapter
        rvGithubUserResult.adapter = githubUserResultAdapter

        val myJson = inputStreamToString(resources.openRawResource(R.raw.github_user))
        Gson().fromJson(myJson, GithubUserModel::class.java).let {
            githubUserAdapter.addAll(it.users)
        }
    }

    private fun initListener() {
        githubUserAdapter.onItemClickListener = { item, _ ->
            val intent = Intent(this, DetailUserActivity::class.java).apply {
                putExtra(Extra.DATA, item)
            }
            startActivityForResult(intent, Extra.FAVORITE)
        }

        githubUserAdapter.onFavButtonClicked = { item, _ ->
            item.isFavorite = if (item.isFavorite == 1) 0 else 1
            val values = ContentValues().apply {
                put(DatabaseContract.GithubUserColumns.USER_NAME, item.username)
                put(DatabaseContract.GithubUserColumns.FULL_NAME, item.name)
                put(DatabaseContract.GithubUserColumns.COMPANY, item.company)
                put(DatabaseContract.GithubUserColumns.FOLLOWER, item.follower)
                put(DatabaseContract.GithubUserColumns.REPOSITORY, item.repository)
                put(DatabaseContract.GithubUserColumns.IS_FAVORITE, item.isFavorite)
            }

            val result = if (item.isFavorite == 1) {
                githubUserFavoriteHelper.insert(values)
            } else {
                githubUserFavoriteHelper.update(item.username, values).toLong()
            }

            if (result > 0) {
                if (result.toInt() == 1 && item.isFavorite == 0) {
                    Snackbar.make(containerMain, getString(R.string.main_favorite_delete_success), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(containerMain, getString(R.string.main_favorite_add_success), Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(containerMain, getString(R.string.main_favorite_failure_add), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        githubUserResultAdapter.onItemClickListener = { item, _ ->
            val intent = Intent(this, DetailUserActivity::class.java).apply {
                putExtra(Extra.DATA, item.login)
            }
            startActivity(intent)
        }
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteItems = defferedFavorite.await()
            if (favoriteItems.isNotEmpty()) {
                githubUserAdapter.itemsFavorite.clear()
                githubUserAdapter.itemsFavorite.addAll(favoriteItems)
                githubUserAdapter.notifyDataSetChanged()
                if (isFromFavorite) {
                    githubUserAdapter.itemsFavoriteTemporary.addAll(favoriteItems)
                }
            }
        }
    }
}
