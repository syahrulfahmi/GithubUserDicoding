package com.sf.gtdng

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sf.gtdng.adapter.GithubUserAdapter
import com.sf.gtdng.adapter.GithubUserResultAdapter
import com.sf.gtdng.model.GithubUserModel
import com.sf.gtdng.utils.Extra
import com.sf.gtdng.utils.hideKeyboard
import com.sf.gtdng.utils.inputStreamToString
import com.sf.gtdng.utils.showAlert
import com.sf.gtdng.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var githubUserResultAdapter: GithubUserResultAdapter
    private lateinit var viewModel: GithubUserViewModel

    private var isChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initObserver()
        initRecyclerView()
        initListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searcView = menu.findItem(R.id.search).actionView as SearchView

        searcView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searcView.apply {
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
                    rvGithubUserResult.visibility = View.GONE
                    textInfo.visibility = View.GONE
                    hideKeyboard(this@MainActivity, searcView)
                    if (query.equals("")) {
                        viewModel.param = query
                        rvGithubUser.visibility = View.VISIBLE
                    } else {
                        rvGithubUser.visibility = View.GONE
                        viewModel.param = query
                        viewModel.getData()
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

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searcView.onActionViewExpanded()
                searcView.setQuery(viewModel.param, false)
                isChange = false
                return true
            }
        })

        return true
    }

    private fun initObserver() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider
                .NewInstanceFactory()
        )
            .get(GithubUserViewModel::class.java)

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
        githubUserResultAdapter.onItemClickListener = { item, _ ->
            val intent = Intent(this, DetailUserResultActivity::class.java).apply {
                putExtra(Extra.DATA, item)
            }
            startActivity(intent)
        }
    }

    fun isConnectivityEnabled(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}
