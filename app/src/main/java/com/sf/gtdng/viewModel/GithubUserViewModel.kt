package com.sf.gtdng.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sf.gtdng.network.Api
import com.sf.gtdng.network.ApiService
import com.sf.gtdng.network.datasource.ApiEndPoint
import com.sf.gtdng.network.response.GithubUserResponse
import com.sf.gtdng.utils.log
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class GithubUserViewModel : ViewModel() {
    private val data = MutableLiveData<GithubUserResponse>()
    var param: String? = null

    fun getData(): LiveData<GithubUserResponse> {
        val retrofit: Retrofit = ApiService.getRetrofitService()
        val apiEndpoint = retrofit.create(ApiEndPoint::class.java)
        val call: Call<GithubUserResponse> = apiEndpoint.getHistoryList(Api.AUTH,param)
        call.enqueue(object : Callback<GithubUserResponse?> {
            override fun onResponse(call: Call<GithubUserResponse?>?,response: Response<GithubUserResponse?>) {
                if (response.code() == 200) {
                    data.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<GithubUserResponse?>?, t: Throwable?) {}
        })
        return data
    }
}