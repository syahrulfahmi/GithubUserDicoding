package com.sf.gtdng

import android.app.SearchManager
import android.content.Context
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
import com.sf.gtdng.utils.inputStreamToString
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

        githubUserAdapter = GithubUserAdapter(this)
        githubUserResultAdapter = GithubUserResultAdapter(this)
        rvGithubUserResult.adapter = githubUserResultAdapter

        initObserver()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searcView = menu.findItem(R.id.search).actionView as SearchView

        searcView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searcView.queryHint = resources.getString(R.string.search_hint)
        searcView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (viewModel.param.isNullOrEmpty()) {
                    rvGithubUser.visibility = View.VISIBLE
                    rvGithubUserResult.visibility = View.GONE
                } else {
                    rvGithubUser.visibility = View.GONE
                    rvGithubUserResult.visibility = View.VISIBLE
                }
                isChange = true
                return true
            }
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                isChange = false
                return true
            }
        })

        return true
    }

    private fun initObserver() {
        viewModel = ViewModelProvider(this,ViewModelProvider
            .NewInstanceFactory())
            .get(GithubUserViewModel::class.java)

            viewModel.getData().observe(this, Observer {
                progressLoading.visibility = View.GONE
                if (it.items.isNotEmpty()) {
                    githubUserResultAdapter.removeAll()
                    rvGithubUser.visibility = View.GONE
                    rvGithubUserResult.visibility = View.VISIBLE
                    githubUserResultAdapter.addAll(it.items)
                } else {
                    textInfo.visibility = View.VISIBLE
                }
            })
    }

    private fun initRecyclerView() {
        val myJson = inputStreamToString(resources.openRawResource(R.raw.github_user))
        Gson().fromJson(myJson, GithubUserModel::class.java).let {
            githubUserAdapter.addAll(it.users)
        }
        rvGithubUser.adapter = githubUserAdapter
    }
}
