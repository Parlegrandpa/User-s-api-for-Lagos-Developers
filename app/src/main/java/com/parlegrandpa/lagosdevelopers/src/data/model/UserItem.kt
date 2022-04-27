package com.parlegrandpa.lagosdevelopers.src.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserItem(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("login")
    val login: String?,

    @SerializedName("avatar_url")
    val avatar_url: String?,

    @SerializedName("type")
    val type: String?,

    @NonNull
    @ColumnInfo(name = "is_favorite")
    val is_favorite: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val uuid: Int
)