package com.parlegrandpa.lagosdevelopers.src.model

import javax.inject.Inject

class UserItemRepository @Inject constructor(private val userItemDao: UserItemDao) {
    fun insertAll(userItem: List<UserItem>) = userItemDao.insertAll(userItem)
    fun getAllUserItems() = userItemDao.getAllUserItems()
    fun getSingleUserItem(userItemID: Int) = userItemDao.getSingleUserItem(userItemID)
    fun removeAllUserItem() = userItemDao.removeAllUserItem()
    fun getAllFavoriteUserItems() = userItemDao.getAllFavoriteUserItems(true)
    fun removeSingleFavoriteUserItem(userItemID: Int) = userItemDao.removeSingleFavoriteUserItem(userItemID, true)
    fun removeAllFavoriteUserItems() = userItemDao.removeAllFavoriteUserItems(false)
}