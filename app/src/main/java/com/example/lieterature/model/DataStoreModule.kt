package com.example.lieterature.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(val dataStore: DataStore<Preferences>) {
    companion object {
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
        val USER_TEXT_KEY = intPreferencesKey("USER_TEXT")
    }

    suspend fun storeInfo(name : String, textSize : Int){
        dataStore.edit {
            it[USER_NAME_KEY] = name
            it[USER_TEXT_KEY] = textSize
        }
    }

    val userNameFlow : Flow<String?> = dataStore.data.map {
        it[USER_NAME_KEY]
    }

    val userTextFlow : Flow<Int?> = dataStore.data.map {
        it[USER_TEXT_KEY]
    }

}