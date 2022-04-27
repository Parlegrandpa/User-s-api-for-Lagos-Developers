package com.parlegrandpa.lagosdevelopers.src.di

import com.parlegrandpa.lagosdevelopers.src.data.service.UsersApi
import com.parlegrandpa.lagosdevelopers.src.data.service.UsersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import dagger.hilt.migration.DisableInstallInCheck

@Module
@DisableInstallInCheck
class ApiModule {

    private val BASE_URL = "https://api.github.com"

    @Provides
    fun provideUserModule(): UsersApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UsersApi::class.java)
    }

    @Provides
    fun provideUserService(): UsersService {
        return UsersService()
    }
}