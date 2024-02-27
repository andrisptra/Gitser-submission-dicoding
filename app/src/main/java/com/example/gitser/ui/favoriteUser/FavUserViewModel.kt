package com.example.gitser.ui.favoriteUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gitser.data.database.FavUserEntity
import com.example.gitser.repository.FavUserRepository

class FavUserViewModel(private val favUserRepository: FavUserRepository) : ViewModel() {

    fun getAllFavUser(): LiveData<List<FavUserEntity>> {
        return favUserRepository.getAllFavUser()
    }
}