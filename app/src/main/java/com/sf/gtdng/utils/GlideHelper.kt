package com.sf.gtdng.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 29/05/20
 */

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load(url).into(this)
}