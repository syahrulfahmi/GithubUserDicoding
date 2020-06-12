package com.sf.consumerapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sf.consumerapp.network.Api
import com.sf.consumerapp.network.ApiService
import com.sf.consumerapp.network.datasource.ApiEndPoint
import com.sf.consumerapp.network.response.*
import com.sf.consumerapp.helper.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class GithubUserViewModel : ViewModel() {
    private val githubUserDetailResponse = MutableLiveData<GithubUserDetailResponse>()
    private var followerListItem = MutableLiveData<ArrayList<FollowingAndFollowerListItem>>()
    private var followingListItem = MutableLiveData<ArrayList<FollowingAndFollowerListItem>>()
    var userName: String? = null

    fun getUserDetail(): LiveData<GithubUserDetailResponse> {
        val retrofit: Retrofit = ApiService.getRetrofitService()
        val apiEndpoint = retrofit.create(ApiEndPoint::class.java)
        val call: Call<GithubUserDetailResponse> = apiEndpoint.getUserDetail(Api.AUTH, userName)
        call.enqueue(object : Callback<GithubUserDetailResponse?> {
            override fun onResponse(
                call: Call<GithubUserDetailResponse?>,
                response: Response<GithubUserDetailResponse?>
            ) {
                if (response.isSuccessful) {
                    githubUserDetailResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<GithubUserDetailResponse?>, t: Throwable) {
                log("ASD", t.message)
            }
        })
        return githubUserDetailResponse
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
                log("ASD", t.message)
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
                log("ASD", t.message)
            }
        })
        return followingListItem
    }
}