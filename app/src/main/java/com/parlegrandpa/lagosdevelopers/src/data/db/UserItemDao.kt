package com.parlegrandpa.lagosdevelopers.src.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.parlegrandpa.lagosdevelopers.src.data.model.UserItem

@Dao
interface UserItemDao {

    @Insert
    fun insertAll(items: List<UserItem>)

    @Query("SELECT * FROM useritem")
    fun getAllUserItems(): List<UserItem>

    @Query("SELECT * FROM useritem WHERE id = :userItemID")
    fun getSingleUserItem(userItemID: Int): UserItem

    @Query("DELETE FROM useritem")
    fun removeAllUserItem()


    @Query("SELECT * FROM useritem WHERE is_favorite = :isFavorite")
    fun getAllFavoriteUserItems(isFavorite: Boolean): List<UserItem>

    @Query("UPDATE useritem SET is_favorite = :isFavorite WHERE id = :userItemID")
    fun removeSingleFavoriteUserItem(userItemID: Int, isFavorite: Boolean): Int

    @Query("UPDATE useritem SET is_favorite = :isFavorite")
    fun removeAllFavoriteUserItems(isFavorite: Boolean)
}