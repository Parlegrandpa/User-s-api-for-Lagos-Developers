package com.parlegrandpa.lagosdevelopers.src.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel

class UserItemViewModelFactory(var application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListUserViewModel::class.java)) {
            return ListUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}