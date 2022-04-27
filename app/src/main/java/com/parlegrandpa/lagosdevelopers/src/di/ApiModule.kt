package com.parlegrandpa.lagosdevelopers.src.di

import android.content.Context
import androidx.room.Room
import com.parlegrandpa.lagosdevelopers.src.model.UserItemDatabase
import com.parlegrandpa.lagosdevelopers.src.model.UsersApi
import com.parlegrandpa.lagosdevelopers.src.model.UsersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

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

//    @Provides
//    fun provideUserItemDatabase(@ApplicationContext context: Context): UserItemDatabase =
//        Room.databaseBuilder(context, UserItemDatabase::class.java, "useritem")
//            .fallbackToDestructiveMigration().build()
}