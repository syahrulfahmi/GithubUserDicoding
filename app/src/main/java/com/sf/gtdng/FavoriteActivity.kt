package com.sf.gtdng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sf.gtdng.db.GithubUserFavoriteHelper
import com.sf.gtdng.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 10/06/20
 */

class FavoriteActivity : AppCompatActivity () {

    private lateinit var githubUserFavoriteHelper: GithubUserFavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        githubUserFavoriteHelper = GithubUserFavoriteHelper.getInstance(this)
        githubUserFavoriteHelper.open()

        loadData()
    }

    private fun loadData () {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedNotes = async(Dispatchers.IO) {
                val cursor = githubUserFavoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = defferedNotes.await()
            if (notes.isNotEmpty()) {
                for (i in notes) {
                    textView.text = i.fullName
                }
            } else {
                Snackbar.make(containerFavorite, "Tidak ada data favorite", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}