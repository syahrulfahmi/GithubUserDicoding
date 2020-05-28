package com.sf.gtdng.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sf.gtdng.network.Api
import com.sf.gtdng.network.ApiService
import com.sf.gtdng.network.datasource.ApiEndPoint
import com.sf.gtdng.network.response.FollowingAndFollowerListItem
import com.sf.gtdng.network.response.FollowingAndFollowerListResponse
import com.sf.gtdng.network.response.GithubUserResponse
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class GithubUserViewModel : ViewModel() {
    private val data = MutableLiveData<GithubUserResponse>()
    private var listItem = MutableLiveData<ArrayList<ItemUserGithub>>()
    private var followerListItem = MutableLiveData<ArrayList<FollowingAndFollowerListItem>>()
    private var followingListItem = MutableLiveData<ArrayList<FollowingAndFollowerListItem>>()
    var param: String? = null
    var userName: String? = null

    fun getData(): LiveData<ArrayList<ItemUserGithub>> {
        val retrofit: Retrofit = ApiService.getRetrofitService()
        val apiEndpoint = retrofit.create(ApiEndPoint::class.java)
        val call: Call<GithubUserResponse> = apiEndpoint.getUserList(Api.AUTH, param)
        call.enqueue(object : Callback<GithubUserResponse?> {
            override fun onResponse(
                call: Call<GithubUserResponse?>?,
                response: Response<GithubUserResponse?>
            ) {
                if (response.isSuccessful) {
                    data.postValue(response.body())
                    val listData = ArrayList<ItemUserGithub>()
                    for (i in response.body()!!.items) {
                        listData.add(i)
                    }
                    listItem.postValue(listData)
                }
            }

            override fun onFailure(call: Call<GithubUserResponse?>?, t: Throwable?) {
                log("ASD", t?.message)
            }
        })
        return listItem
    }

    fun getUserFollower(): LiveData<ArrayList<FollowingAndFollowerListItem>> {
        val retrofit: Retrofit = ApiService.getRetrofitService()
        val apiEndpoint = retrofit.create(ApiEndPoint::class.java)
        val call: Call<FollowingAndFollowerListResponse> = apiEndpoint.getUserFollower(Api.AUTH, userName)
        call.enqueue(object : Callback<FollowingAndFollowerListResponse?> {
            override fun onResponse(
                call: Call<FollowingAndFollowerListResponse?>?,
                response: Response<FollowingAndFollowerListResponse?>
            ) {
                if (response.isSuccessful) {
                    val listData = ArrayList<FollowingAndFollowerListItem>()
                    for (i in response.body()!!) {
                        listData.add(i)
                    }
                    followerListItem.postValue(listData)
                }
            }

            override fun onFailure(call: Call<FollowingAndFollowerListResponse?>, t: Throwable) {
                log("ASD", t?.message)
            }
        })
        return followerListItem
    }

    fun getUserFollowing(): LiveData<ArrayList<FollowingAndFollowerListItem>> {
        val retrofit: Retrofit = ApiService.getRetrofitService()
        val apiEndpoint = retrofit.create(ApiEndPoint::class.java)
        val call: Call<FollowingAndFollowerListResponse> = apiEndpoint.getUserFollowing(Api.AUTH, userName)
        call.enqueue(object : Callback<FollowingAndFollowerListResponse?> {
            override fun onResponse(
                call: Call<FollowingAndFollowerListResponse?>?,
                response: Response<FollowingAndFollowerListResponse?>
            ) {
                if (response.isSuccessful) {
                    val listData = ArrayList<FollowingAndFollowerListItem>()
                    for (i in response.body()!!) {
                        listData.add(i)
                    }
                    followingListItem.postValue(listData)
                }
            }

            override fun onFailure(call: Call<FollowingAndFollowerListResponse?>, t: Throwable) {
                log("ASD", t?.message)
            }
        })
        return followingListItem
    }
}