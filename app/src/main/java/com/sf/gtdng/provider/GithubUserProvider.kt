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

        /*
        Integer digunakan sebagai identifier antara select all sama select by id
         */
        private const val NOTE = 1
        private const val NOTE_ID = 2
        private lateinit var noteHelper: GithubUserFavoriteHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        /*
        Uri matcher untuk mempermudah identifier dengan menggunakan integer
        misal
        uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
        uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
         */
        init {
            // content://com.dicoding.picodiploma.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)

            // content://com.dicoding.picodiploma.mynotesapp/note/id
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                NOTE_ID)
        }
    }

    override fun onCreate(): Boolean {
        noteHelper = GithubUserFavoriteHelper.getInstance(context as Context)
        noteHelper.open()
        return true
    }

    /*
    Method queryAll digunakan ketika ingin menjalankan queryAll Select
    Return cursor
     */
    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            NOTE -> noteHelper.queryAll()
            NOTE_ID -> noteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (NOTE) {
            sUriMatcher.match(uri) -> noteHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> noteHelper.deleteByUserName(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}