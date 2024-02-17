package com.example.gitser.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavUserEntity)

    @Delete
    fun delete(user: FavUserEntity)

    @Query("SELECT * from favuserentity")
    fun getAllFavUserList(): LiveData<List<FavUserEntity>>

    @Query("SELECT * FROM favuserentity WHERE username = :username")
    fun getFavUserByUsername(username: String): LiveData<FavUserEntity>
}