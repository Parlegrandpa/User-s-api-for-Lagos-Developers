package com.parlegrandpa.lagosdevelopers.src.di

import com.parlegrandpa.lagosdevelopers.src.data.service.UsersService
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: UsersService)

    fun inject(viewModel: ListUserViewModel)
}