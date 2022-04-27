package com.parlegrandpa.lagosdevelopers.src.data.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parlegrandpa.lagosdevelopers.src.viewmodel.UserItemDetailViewModel

@Suppress("UNCHECKED_CAST")
class UserItemDetailModelFactory(var application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserItemDetailViewModel::class.java)) {
            return UserItemDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}