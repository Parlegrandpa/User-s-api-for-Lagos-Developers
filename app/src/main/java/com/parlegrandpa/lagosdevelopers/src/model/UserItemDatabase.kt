package com.parlegrandpa.lagosdevelopers.src.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserItem::class], version = 1, exportSchema = false)
abstract class UserItemDatabase: RoomDatabase() {
    abstract fun userItemDao(): UserItemDao

    companion object
    {
        private var INSTANCE: UserItemDatabase? = null
        fun getInstance(context: Context): UserItemDatabase {
            if (INSTANCE == null) {
                synchronized(UserItemDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserItemDatabase::class.java,
                "useritem"
            ).build()
    }
}