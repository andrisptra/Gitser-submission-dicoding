package com.example.gitser.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitser.di.Injection
import com.example.gitser.repository.FavUserRepository
import com.example.gitser.ui.favoriteUser.FavUserViewModel
import com.example.gitser.ui.userDetail.UserDetailViewModel

class ViewModelFactory private constructor(private val favUserRepository: FavUserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(favUserRepository) as T
        } else if (modelClass.isAssignableFrom(FavUserViewModel::class.java)) {
            return FavUserViewModel(favUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }

    }
}