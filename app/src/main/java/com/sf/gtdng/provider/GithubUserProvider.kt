package com.sf.gtdng.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.AUTHORITY
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.TABLE_NAME
import com.sf.gtdng.db.GithubUserFavoriteHelper

class GithubUserProvider : ContentProvider() {

    companion object {
        private const val GITHUB_FAVORITE = 1
        private const val GITHUB_FAVORITE_ID = 2
        private lateinit var noteHelper: GithubUserFavoriteHelper
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, GITHUB_FAVORITE)

            uriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                GITHUB_FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        noteHelper = GithubUserFavoriteHelper.getInstance(context as Context)
        noteHelper.open()
        return true
    }

    /* Method queryAll digunakan ketika ingin menjalankan queryAll Select
    yang akan mengembalikan keluaran cursor */
    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (uriMatcher.match(uri)) {
            GITHUB_FAVORITE -> noteHelper.queryAll()
            GITHUB_FAVORITE_ID -> noteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (GITHUB_FAVORITE) {
            uriMatcher.match(uri) -> noteHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri,values: ContentValues?,selection: String?,selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (GITHUB_FAVORITE_ID) {
            uriMatcher.match(uri) -> noteHelper.deleteByUserName(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}