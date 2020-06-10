package com.sf.gtdng.helper

import android.database.Cursor
import com.sf.gtdng.db.DatabaseContract
import com.sf.gtdng.entity.GithubUserField

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 10/06/20
 */

object MappingHelper {
    fun mapCursorToArrayList(githubUserFavoriteCursor: Cursor?): ArrayList<GithubUserField> {
        val userList = ArrayList<GithubUserField>()
        githubUserFavoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns._ID))
                val userName = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.USER_NAME))
                val fullName = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.FULL_NAME))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.COMPANY))
                val follower = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.FOLLOWER))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.REPOSITORY))
                val favorite = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.IS_FAVORITE))
                userList.add(
                    GithubUserField(
                        id,
                        userName,
                        fullName,
                        company,
                        follower,
                        repository,
                        favorite
                    )
                )
            }
        }
        return userList
    }
}