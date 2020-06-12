package com.sf.gtdng.db

import android.net.Uri
import android.provider.BaseColumns

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 09/06/20
 */

internal class DatabaseContract {

    class GithubUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "github_user"
            const val _ID = "id"
            const val USER_NAME = "user_name"
            const val FULL_NAME = "full_name"
            const val COMPANY = "company"
            const val FOLLOWER = "follower"
            const val REPOSITORY = "repository"
            const val IS_FAVORITE = "is_favorite"

            const val AUTHORITY = "com.sf.gtdng"
            const val SCHEME = "content"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}