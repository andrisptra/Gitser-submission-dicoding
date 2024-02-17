package com.example.gitser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitser.data.response.ItemsItem
import com.example.gitser.data.response.ResponseGithubSearch
import com.example.gitser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Boolean>()
    val message: LiveData<Boolean> = _message

    fun searchUser(key: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(key)
        client.enqueue(object : Callback<ResponseGithubSearch> {
            override fun onResponse(
                call: Call<ResponseGithubSearch>, response: Response<ResponseGithubSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody.items
                    }
                    if (responseBody?.totalCount == 0) {
                        _message.value = true
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseGithubSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}