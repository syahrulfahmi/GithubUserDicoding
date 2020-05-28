package com.sf.gtdng

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.sf.gtdng.adapter.TabFragmentAdapter
import com.sf.gtdng.fragment.FollowerFragment
import com.sf.gtdng.fragment.FollowingFragment
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.utils.Extra
import com.sf.gtdng.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.activity_detail_user_result.*

class DetailUserResultActivity : AppCompatActivity() {

    private lateinit var tabFragmentAdapter : TabFragmentAdapter
    private lateinit var viewModel: GithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_result)

        imageHeader.drawable.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY)
//        imageHeader.setColorFilter("0x76ffffff", PorterDuff.Mode.MULTIPLY);
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GithubUserViewModel::class.java)
        val item = intent.getParcelableExtra<ItemUserGithub>(Extra.DATA)
        viewModel.userName = item.login

        tabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        tabFragmentAdapter.addFragment(FollowerFragment(), "Pengikut")
        tabFragmentAdapter.addFragment(FollowingFragment(), "Mengikuti")
        viewPager.adapter = tabFragmentAdapter
        tabs.setupWithViewPager(viewPager)
    }
}