package com.parlegrandpa.lagosdevelopers.src.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parlegrandpa.lagosdevelopers.src.model.UserItem
import com.parlegrandpa.lagosdevelopers.src.model.UserItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class UserItemDetailViewModel constructor(application: Application): ViewModel() {

    val user = MutableLiveData<UserItem>()
    var isUpdated = MutableLiveData<Boolean>()

    private val userItemDatabase = UserItemDatabase.getInstance(application)


    fun refresh (id: Int) {
        fetchDataFromDatabase(id)
    }

    fun fetchDataFromDatabase(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().getSingleUserItem(id)
            Log.e("DSDSDS", userItems.toString())
            userItemsRetrieved(userItems)
        }
    }

    fun updateFavorite(id: Int, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().removeSingleFavoriteUserItem(id, isFavorite)
            Log.e("DSDSDSghf", userItems.toString())
            isUpdated.postValue(userItems > 0)
        }
    }

    private fun userItemsRetrieved(userItem: UserItem) {
        user.postValue(userItem)
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).cancel()
    }
}