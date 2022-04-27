package com.parlegrandpa.lagosdevelopers.src.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parlegrandpa.lagosdevelopers.src.di.DaggerApiComponent
import com.parlegrandpa.lagosdevelopers.src.data.db.UserItemDatabase
import com.parlegrandpa.lagosdevelopers.src.data.model.User
import com.parlegrandpa.lagosdevelopers.src.data.model.UserItem
import com.parlegrandpa.lagosdevelopers.src.data.service.UsersService
import com.parlegrandpa.lagosdevelopers.src.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    private val refreshTime: Long = 24 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()

    private val userItemDatabase = UserItemDatabase.getInstance(application)
    private var sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(application)

    val users = MutableLiveData<List<UserItem>>()
    val usersLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(forceLoadFromRemote: Boolean) {
        val updateTime = sharedPreferencesHelper.getUpdateTime()
        val currentTime = System.nanoTime()

        if (forceLoadFromRemote) {
            fetchUsersFromRemote()
        } else {
            if (updateTime != null) {
                if (updateTime > 0.0 && currentTime - updateTime < refreshTime) {
                    fetchUsersFromDatabase()
                } else {
                    fetchUsersFromRemote()
                }
            } else {
                fetchUsersFromRemote()
            }
        }
    }

    private fun fetchUsersFromRemote() {
        loading.postValue(true)
        disposable.add(
            usersService.getUsers(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<User>() {
                    override fun onSuccess(value: User) {
                        value.userItems?.let {
                            insertUsersFromDatabase(it)
                            sharedPreferencesHelper.saveUpdateTime(System.nanoTime())
                        }
                    }

                    override fun onError(e: Throwable) {
                        usersLoadError.postValue(true)
                        loading.postValue(false)
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
        CoroutineScope(Dispatchers.IO).launch {
            userItemDatabase.userItemDao().removeAllUserItem()
            userItemDatabase.userItemDao().insertAll(newList)
            userItemsRetrieved(newList)
        }
    }

    private fun fetchUsersFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().getAllUserItems()
            userItemsRetrieved(userItems)
        }
    }

    fun fetchFavoriteList() {
        CoroutineScope(Dispatchers.IO).launch {
            val userItems = userItemDatabase.userItemDao().getAllFavoriteUserItems(true)
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