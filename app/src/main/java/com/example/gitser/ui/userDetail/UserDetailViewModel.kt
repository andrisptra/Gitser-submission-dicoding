package com.example.gitser.ui.userDetail

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitser.data.database.FavUserEntity
import com.example.gitser.data.response.DetailUserResponse
import com.example.gitser.data.retrofit.ApiConfig
import com.example.gitser.repository.FavUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val favUserRepository: FavUserRepository) : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun insert(user: FavUserEntity) {
        favUserRepository.insert(user)
    }

    fun delete(user: FavUserEntity) {
        favUserRepository.delete(user)
    }

    fun getUserFavByUsername(username: String): LiveData<FavUserEntity> {
        return favUserRepository.getFavUserByUsername(username)
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>, response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }

        })
    }

    companion object {
        private const val TAG = "UserDetailViewModel"
    }

}