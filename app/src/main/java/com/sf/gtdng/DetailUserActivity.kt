package com.sf.gtdng

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.sf.gtdng.adapter.TabFragmentAdapter
import com.sf.gtdng.db.DatabaseContract
import com.sf.gtdng.db.GithubUserFavoriteHelper
import com.sf.gtdng.fragment.DialogNoConnection
import com.sf.gtdng.fragment.FollowerFragment
import com.sf.gtdng.fragment.FollowingFragment
import com.sf.gtdng.network.NetworkChecking
import com.sf.gtdng.helper.Extra
import com.sf.gtdng.helper.loadUrl
import com.sf.gtdng.model.User
import com.sf.gtdng.viewModel.GithubUserViewModel
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.item_github_user.view.*
import kotlinx.android.synthetic.main.layout_shimmer_user_detail.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var viewModel: GithubUserViewModel
    private lateinit var githubUserFavoriteHelper: GithubUserFavoriteHelper
    private var itemUserGithub: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        githubUserFavoriteHelper = GithubUserFavoriteHelper.getInstance(this)
        githubUserFavoriteHelper.open()

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(GithubUserViewModel::class.java)
        itemUserGithub = intent.getParcelableExtra(Extra.DATA)
        viewModel.userName = itemUserGithub?.username

        initData()
        initView()
        checkConnection()
    }

    private fun initData() {
        shimmerUserDetail.startShimmerAnimation()
        shimmerUserDetail.visibility = View.VISIBLE
        viewModel.getUserDetail().observe(this, Observer {
            shimmerUserDetail.stopShimmerAnimation()
            shimmerUserDetail.visibility = View.GONE
            containerHeader.visibility = View.VISIBLE
            it.name?.let { name ->
                textFullName.text = name
                textToolbarUserName.text = name
            } ?: run {
                textFullName.text = getString(R.string.app_name)
                textToolbarUserName.text = getString(R.string.app_name)
            }
            it.login?.let { userName ->
                textUserName.text = userName
            }
            it.company?.let { company ->
                textCompanyName.text = company
            }?: run {
                textCompanyName.text = getString(R.string.detail_user_company_not_found)
            }
            it.location?.let { location ->
                textLocation.text = location
            } ?: run {
                containerLocation.visibility = View.GONE
            }
            tvRepository.text = it.publicRepos.toString()
            tvFollower.text = it.followers.toString()
            tvFollowing.text = it.following.toString()
            imageUser.loadUrl(it.avatarUrl)
        })
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapsingLayout.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingLayout)) {
                textToolbarUserName.animate().alpha((1).toFloat())
            } else {
                textToolbarUserName.animate().alpha((0).toFloat())
            }
        })
        val tabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        tabFragmentAdapter.addFragment(FollowerFragment(), getString(R.string.detail_user_follower))
        tabFragmentAdapter.addFragment(FollowingFragment(),getString(R.string.detail_user_following))
        viewPager.adapter = tabFragmentAdapter
        tabs.setupWithViewPager(viewPager)

        buttonFav.isChecked = itemUserGithub?.isFavorite == 1
        buttonFav.setEventListener(object : SparkEventListener {
            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {}
            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {}
            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                itemUserGithub?.apply{
                    isFavorite = if (isFavorite == 1) 0 else 1
                    val values = ContentValues().apply {
                        put(DatabaseContract.GithubUserColumns.USER_NAME, username)
                        put(DatabaseContract.GithubUserColumns.FULL_NAME, name)
                        put(DatabaseContract.GithubUserColumns.COMPANY, company)
                        put(DatabaseContract.GithubUserColumns.FOLLOWER, follower)
                        put(DatabaseContract.GithubUserColumns.REPOSITORY, repository)
                        put(DatabaseContract.GithubUserColumns.IS_FAVORITE, isFavorite)
                    }

                    val result = if (isFavorite == 1) {
                        githubUserFavoriteHelper.insert(values)
                    } else {
                        githubUserFavoriteHelper.update(username, values).toLong()
                    }

                    if (result > 0) {
                        if (result.toInt() == 1 && isFavorite == 0) {
                            Snackbar.make(coordinatorLayout, getString(R.string.main_favorite_delete_success), Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(coordinatorLayout, getString(R.string.main_favorite_add_success), Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        Snackbar.make(coordinatorLayout, getString(R.string.main_favorite_failure_add), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun checkConnection() {
        //check connection is available or not
        if (NetworkChecking.isNetworkAvailable(this)) {
            viewModel.getUserDetail()
            viewModel.getUserFollower()
            viewModel.getUserFollowing()
        } else {
            val dialog = DialogNoConnection()
            dialog.onButtonClickListener = {
                if(NetworkChecking.isNetworkAvailable(this)) {
                    dialog.dismiss()
                    viewModel.getUserDetail()
                    viewModel.getUserFollower()
                    viewModel.getUserFollowing()
                }
            }
            dialog.onBackPressed = { finish() }
            dialog.show(supportFragmentManager, DialogNoConnection.TAG)
        }
    }
}