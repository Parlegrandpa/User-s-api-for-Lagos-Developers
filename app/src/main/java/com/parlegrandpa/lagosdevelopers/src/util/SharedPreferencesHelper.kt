package com.parlegrandpa.lagosdevelopers.src.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferencesHelper constructor(context: Context) {

    private val PREF_TIME = "pref_time"
    private val PREF_NAME = "preferences"
    private var sharedPreferences: SharedPreferences? = null
    private var prefEditor: SharedPreferences.Editor? = null

    init {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context, PREF_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveUpdateTime(value: Long) {
        doEdit()
        prefEditor?.putLong(PREF_TIME, value)
        doCommit()
    }

    fun getUpdateTime(): Long? {
        return sharedPreferences?.getLong(PREF_TIME, 0)
    }

    fun remove(key: String) {
        doEdit()
        prefEditor?.remove(key)
        doCommit()
    }

    fun contains(key: String): Boolean? {
        return sharedPreferences?.contains(key)
    }

    fun clear() {
        doEdit()
        prefEditor?.clear()
        doCommit()
    }

    @SuppressLint("CommitPrefEdits")
    private fun doEdit() {

        if (prefEditor == null) {

            prefEditor = sharedPreferences?.edit()
        }
    }

    private fun doCommit() {

        if (prefEditor != null) {

            prefEditor?.commit()

            prefEditor = null
        }
    }
}