package com.sf.consumerapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import com.sf.consumerapp.adapter.FavoriteListAdapter
import com.sf.consumerapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.sf.consumerapp.helper.Extra
import com.sf.consumerapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var favoriteListAdapter: FavoriteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()
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

    private fun initRecyclerView() {
        favoriteListAdapter = FavoriteListAdapter()
        rvGithubUser.adapter = favoriteListAdapter
    }

    private fun initListener() {
        favoriteListAdapter.onItemClickListener = { item, _ ->
            val intent = Intent(this, DetailUserActivity::class.java).apply {
                putExtra(Extra.DATA, item.userName)
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
                favoriteListAdapter.addAll(favoriteItems)
            }
        }
    }
}
