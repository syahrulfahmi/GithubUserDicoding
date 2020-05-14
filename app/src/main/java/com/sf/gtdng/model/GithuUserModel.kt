package com.sf.gtdng.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class GithubUserModel(
    @SerializedName("users")
    val users: List<User>
)

@Parcelize
data class User(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("follower")
    val follower: Int,
    @SerializedName("following")
    val following: Int,
    @SerializedName("location")
    val location: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("repository")
    val repository: Int,
    @SerializedName("username")
    val username: String
): Parcelable