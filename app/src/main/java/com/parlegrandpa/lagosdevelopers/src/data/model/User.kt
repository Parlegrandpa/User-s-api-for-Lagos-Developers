package com.parlegrandpa.lagosdevelopers.src.data.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("items")
    val userItems: List<UserItem>?
)