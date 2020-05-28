package com.sf.gtdng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sf.gtdng.adapter.TabFragmentAdapter
import com.sf.gtdng.fragment.FollowerFragment
import com.sf.gtdng.fragment.FollowingFragment
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.utils.Extra
import com.sf.gtdng.utils.loadUrl
import com.sf.gtdng.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.activity_detail_user_result.*

class DetailUserResultActivity : AppCompatActivity() {

    private lateinit var viewModel: GithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_result)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GithubUserViewModel::class.java)
        val item = intent.getParcelableExtra<ItemUserGithub>(Extra.DATA)
        viewModel.userName = item.login

        initData()
        initView()
    }

    private fun initData () {
        viewModel.getUserDetail().observe(this, Observer {
            textFullName.text = it.name
            imageUser.loadUrl(it.avatarUrl)
        })
    }

    private fun initView () {
        val tabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        tabFragmentAdapter.addFragment(FollowerFragment(), "Pengikut")
        tabFragmentAdapter.addFragment(FollowingFragment(), "Mengikuti")
        viewPager.adapter = tabFragmentAdapter
        tabs.setupWithViewPager(viewPager)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}