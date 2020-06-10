package com.sf.gtdng.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 09/06/20
 */

@Parcelize
data class GithubUserField (
    var id: Int = 0,
    var userName: String = "",
    var fullName: String = "",
    var company: String = "",
    var follower: Int = 0,
    var repository: Int = 0,
    var isFavorite: Int = 0
): Parcelable