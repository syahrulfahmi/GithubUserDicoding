package com.sf.consumerapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.sf.consumerapp.adapter.TabFragmentAdapter
import com.sf.consumerapp.fragment.DialogNoConnection
import com.sf.consumerapp.fragment.FollowerFragment
import com.sf.consumerapp.fragment.FollowingFragment
import com.sf.consumerapp.network.NetworkChecking
import com.sf.consumerapp.helper.Extra
import com.sf.consumerapp.helper.loadUrl
import com.sf.consumerapp.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.layout_shimmer_user_detail.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var viewModel: GithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(GithubUserViewModel::class.java)
        val userName = intent.getStringExtra(Extra.DATA)
        viewModel.userName = userName

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