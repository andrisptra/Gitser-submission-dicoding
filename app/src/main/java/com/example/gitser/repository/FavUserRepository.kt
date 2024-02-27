package com.example.gitser.repository

import androidx.lifecycle.LiveData
import com.example.gitser.data.database.FavUserDao
import com.example.gitser.data.database.FavUserEntity
import com.example.gitser.helper.AppExecutors

class FavUserRepository(
    private val favUserDao: FavUserDao,
    private val appExecutors: AppExecutors
) {
    fun getAllFavUser(): LiveData<List<FavUserEntity>> = favUserDao.getAllFavUserList()

    fun getFavUserByUsername(username: String): LiveData<FavUserEntity> =
        favUserDao.getFavUserByUsername(username)

    fun insert(user: FavUserEntity) {
        appExecutors.diskIO.execute { favUserDao.insert(user) }
    }

    fun delete(user: FavUserEntity) {
        appExecutors.diskIO.execute { favUserDao.delete(user) }
    }

    companion object {
        @Volatile
        private var instance: FavUserRepository? = null
        fun getInstance(
            favUserDao: FavUserDao,
            appExecutors: AppExecutors
        ): FavUserRepository = instance ?: synchronized(this) {
            instance ?: FavUserRepository(favUserDao, appExecutors)
        }.also { instance = it }
    }

}