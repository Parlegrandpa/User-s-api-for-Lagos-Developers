package com.parlegrandpa.lagosdevelopers.src.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("items")
    val userItems: List<UserItem>?
)