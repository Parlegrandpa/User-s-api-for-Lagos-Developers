package com.parlegrandpa.lagosdevelopers.src.data.service

import com.parlegrandpa.lagosdevelopers.src.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("search/users?q=lagos")
    fun getUsers(@Query("page") pageID:Int): Single<User>
}