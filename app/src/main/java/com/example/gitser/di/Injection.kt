package com.example.gitser.di

import android.content.Context
import com.example.gitser.data.database.FavUserDatabase
import com.example.gitser.helper.AppExecutors
import com.example.gitser.repository.FavUserRepository

object Injection {
    fun provideRepository(context: Context): FavUserRepository {
        val database = FavUserDatabase.getDatabase(context)
        val dao = database.favUserDao()
        val appExecutors = AppExecutors()
        return FavUserRepository.getInstance(dao,appExecutors)
    }
}