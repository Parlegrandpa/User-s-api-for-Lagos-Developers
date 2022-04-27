package com.parlegrandpa.lagosdevelopers.src.model

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    @GET("search/users?q=lagos")
    fun getUsers(@Query("page") pageID:Int): Single<User>
}