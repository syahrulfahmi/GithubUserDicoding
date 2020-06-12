package com.sf.gtdng

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sf.gtdng.adapter.FavoriteListAdapter
import com.sf.gtdng.db.DatabaseContract
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.sf.gtdng.db.GithubUserFavoriteHelper
import com.sf.gtdng.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_favorite.textInfo
import kotlinx.android.synthetic.main.activity_favorite.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 10/06/20
 */

class FavoriteActivity : AppCompatActivity() {

    private lateinit var githubUserFavoriteHelper: GithubUserFavoriteHelper
    private lateinit var favoriteListAdapter: FavoriteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        githubUserFavoriteHelper = GithubUserFavoriteHelper.getInstance(this)
        githubUserFavoriteHelper.open()

        initRecylerView()
        loadData()
        initListener()
    }

    private fun initListener() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
        favoriteListAdapter.onUnFavButtonClicked = { item, _ ->
            item.isFavorite = if (item.isFavorite == 1) 0 else 1
            val values = ContentValues().apply {
                put(DatabaseContract.GithubUserColumns.USER_NAME, item.userName)
                put(DatabaseContract.GithubUserColumns.FULL_NAME, item.fullName)
                put(DatabaseContract.GithubUserColumns.COMPANY, item.company)
                put(DatabaseContract.GithubUserColumns.FOLLOWER, item.follower)
                put(DatabaseContract.GithubUserColumns.REPOSITORY, item.repository)
                put(DatabaseContract.GithubUserColumns.IS_FAVORITE, item.isFavorite)
            }
            val result = githubUserFavoriteHelper.update(item.userName,values).toLong()

            if (result > 0) {
                Snackbar.make(containerFavorite,getString(R.string.main_favorite_delete_success),Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                Snackbar.make(containerFavorite,getString(R.string.main_favorite_failure_delete),Snackbar.LENGTH_SHORT)
                    .show()
            }

            if (favoriteListAdapter.items.isEmpty()) {
                textInfo.visibility = View.VISIBLE
                rvListFavorite.visibility = View.GONE
            }
        }
    }

    private fun initRecylerView() {
        favoriteListAdapter = FavoriteListAdapter(this)
        rvListFavorite.adapter = favoriteListAdapter
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteItems = defferedFavorite.await()
            if (favoriteItems.isNotEmpty()) {
                favoriteListAdapter.items.clear()
                for (i in favoriteItems) {
                    if (i.isFavorite == 1) {
                        favoriteListAdapter.add(i)
                    }
                }
            } else {
                textInfo.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}