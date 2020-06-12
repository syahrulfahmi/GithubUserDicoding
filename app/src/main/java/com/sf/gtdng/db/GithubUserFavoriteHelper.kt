package com.sf.gtdng.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.TABLE_NAME
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.USER_NAME
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion._ID
import java.sql.SQLException

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 09/06/20
 */

class GithubUserFavoriteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: GithubUserFavoriteHelper? = null
        fun getInstance(context: Context): GithubUserFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubUserFavoriteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(userName: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USER_NAME = ?", arrayOf(userName))
    }

    fun deleteByUserName(userName: String): Int {
        return database.delete(DATABASE_TABLE, "$USER_NAME = '$userName'", null)
    }
}