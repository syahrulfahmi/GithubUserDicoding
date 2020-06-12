package com.sf.consumerapp.helper

import com.sf.consumerapp.BuildConfig


object AppLog {
    @JvmStatic
    val ENABLE_LOG = BuildConfig.DEBUG
}

object Extra {
    const val DATA = "extra_data"
    const val FAVORITE = 909
}