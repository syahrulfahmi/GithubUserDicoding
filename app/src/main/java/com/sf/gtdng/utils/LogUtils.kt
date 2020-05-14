package com.sf.gtdng.utils

import android.util.Log

fun log(tag: String, message: String?) {
    if (AppLog.ENABLE_LOG) Log.d(tag, message ?: "null")
}