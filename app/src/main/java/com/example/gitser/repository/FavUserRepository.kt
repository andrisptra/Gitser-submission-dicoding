package com.example.gitser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.gitser.data.database.FavUserDao
import com.example.gitser.data.database.FavUserDatabase
import com.example.gitser.data.database.FavUserEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {
    private val mFavUserDao: FavUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUserDatabase.getDatabase(application)
        mFavUserDao = db.favUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavUserEntity>> =
        mFavUserDao.getAllFavUserList()

    fun getFavUserByUsername(username: String): LiveData<FavUserEntity> =
        mFavUserDao.getFavUserByUsername(username)

    fun insert(user: FavUserEntity) {
        executorService.execute { mFavUserDao.insert(user) }
    }

    fun delete(user: FavUserEntity) {
        executorService.execute { mFavUserDao.delete(user) }
    }
}