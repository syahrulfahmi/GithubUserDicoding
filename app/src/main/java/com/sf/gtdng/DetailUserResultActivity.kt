package com.sf.gtdng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.utils.Extra
import com.sf.gtdng.utils.log

class DetailUserResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_result)

        val item = intent.getParcelableExtra<ItemUserGithub>(Extra.DATA)
        log("ASD", item?.toString())
    }
}