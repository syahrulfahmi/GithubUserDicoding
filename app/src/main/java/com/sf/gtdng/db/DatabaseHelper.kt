package com.sf.gtdng.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sf.gtdng.db.DatabaseContract.GithubUserColumns.Companion.TABLE_NAME

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 09/06/20
 */

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_github_user"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_GITHUB_USER_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.GithubUserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.GithubUserColumns.USER_NAME} TEXT NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.FULL_NAME} TEXT NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.FOLLOWER} INTEGER NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.REPOSITORY} INTEGER NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.IS_FAVORITE} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_GITHUB_USER_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}