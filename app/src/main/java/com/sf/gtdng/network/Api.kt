package com.sf.gtdng.network

import android.os.Build
import com.sf.gtdng.BuildConfig


object Api {
    const val BASE_URL = "https://api.github.com/"
    const val SEARCH_USER = "search/users{{ username }}"
    const val AUTH = BuildConfig.AUTH
}