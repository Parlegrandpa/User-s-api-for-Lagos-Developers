package com.parlegrandpa.lagosdevelopers.src.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parlegrandpa.lagosdevelopers.src.di.DaggerApiComponent
import com.parlegrandpa.lagosdevelopers.src.model.*
import com.parlegrandpa.lagosdevelopers.src.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.inject.Inject

class ListUserViewModel(application: Application) : ViewModel() {

    @Inject
    lateinit var usersService: UsersService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val refreshTime: Long = 5 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()

    private var userList = ArrayList<UserItem>()
    private val userItemDatabase = UserItemDatabase.getInstance(application)
    private var sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(application)

    val users = MutableLiveData<List<UserItem>>()
    val usersLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(pathId: Int, showLoader: Boolean) {
        val updateTime = sharedPreferencesHelper.getUpdateTime()
        val currentTime = System.nanoTime()

        if (updateTime != null) {
            if (updateTime > 0.0 && currentTime - updateTime < refreshTime) {
                fetchUsersFromDatabase()
            } else {
                fetchUsersFromRemote(pathId, showLoader)
            }
        } else {
            fetchUsersFromRemote(pathId, showLoader)
        }
    }

    private fun fetchUsersFromRemote(pathId: Int, showLoader: Boolean) {
        loading.value = showLoader
        disposable.add(
            usersService.getUsers(pathId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<User>() {
                    override fun onSuccess(value: User) {
//                        userList = value.userItems as ArrayList<UserItem>
//
//                        if (!showLoader) {
//                            users.value = userList
//                        } else {
//                            users.value = value.userItems
//                        }
//                        usersLoadError.value = false
//                        loading.value = false

                        value.userItems?.let {
                            insertUsersFromDatabase(it)
                            sharedPreferencesHelper.saveUpdateTime(System.nanoTime())
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("SDFWF", e.message.toString())
                        usersLoadError.value = true
                        loading.value = false
                    }

                })
        )
    }

    private fun userItemsRetrieved(user: List<UserItem>) {
        users.postValue(user)
        usersLoadError.postValue(false)
        loading.postValue(false)
    }

    private fun insertUsersFromDatabase(newList: List<UserItem>) {
        Log.e("DSDSDSsds", newList.toString())
        CoroutineScope(Dispatchers.IO).launch {
            userItemDatabase.userItemDao().removeAllUserItem()
            userItemDatabase.userItemDao().insertAll(newList)

            userItemsRetrieved(newList)
        }
    }

    private fun fetchUsersFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().getAllUserItems()
            Log.e("DSDSDS", userItems.toString())
            userItemsRetrieved(userItems)
        }
    }

    fun fetchFavoriteList() {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().getAllFavoriteUserItems(true)
            Log.e("DSDSDS", userItems.toString())
            userItemsRetrieved(userItems)
        }
    }

    fun removeAllFavoriteList() {
        CoroutineScope(Dispatchers.IO).launch {
            userItemDatabase.userItemDao().removeAllFavoriteUserItems(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        CoroutineScope(Dispatchers.IO).cancel()
    }
}