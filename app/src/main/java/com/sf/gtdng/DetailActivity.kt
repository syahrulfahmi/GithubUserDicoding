package com.sf.gtdng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sf.gtdng.model.User
import com.sf.gtdng.utils.Extra
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_section_github_user_statistic.*
import kotlinx.android.synthetic.main.layout_section_personal.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getData()
    }

    private fun getData () {
        val item = intent.getParcelableExtra<User>(Extra.DATA)
        if (item != null) {
            tvUserName.text = item.username
            tvFullName.text = item.name
            tvCompanyName.text = item.company
            tvLocation.text = item.location
            tvRepository.text= item.repository.toString()
            tvFollower.text= item.follower.toString()
            tvFollowing.text= item.following.toString()
            val imageResource = resources.getIdentifier(item.avatar, null, packageName)
            imageGithubUser.setImageDrawable(getDrawable(imageResource))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}