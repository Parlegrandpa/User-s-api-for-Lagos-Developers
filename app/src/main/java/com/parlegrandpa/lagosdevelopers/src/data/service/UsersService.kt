package com.parlegrandpa.lagosdevelopers.src.data.service

import com.parlegrandpa.lagosdevelopers.src.data.model.User
import com.parlegrandpa.lagosdevelopers.src.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class UsersService {

    @Inject
    lateinit var api: UsersApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getUsers(pageID: Int): Single<User> {
        return api.getUsers(pageID)
    }
}