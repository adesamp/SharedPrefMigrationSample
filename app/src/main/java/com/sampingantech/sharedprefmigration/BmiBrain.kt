package com.sampingantech.sharedprefmigration

import android.app.Activity
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

class BmiBrain(activity: Activity) {

    companion object {
        private val NAME_KEY = stringPreferencesKey("NAME_KEY")
        private val HEIGHT_KEY = floatPreferencesKey("HEIGHT_KEY")
        private val WEIGHT_KEY = intPreferencesKey("WEIGHT_KEY")
        private val IS_MALE_KEY = booleanPreferencesKey("IS_MALE_KEY")
        private val IS_FEMALE_KEY = booleanPreferencesKey("IS_FEMALE_KEY")
        private val BMI_KEY = floatPreferencesKey("BMI_KEY")
    }

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "sampingan_data_store",
        produceMigrations = ::sharedPreferencesMigration
    )

    private fun sharedPreferencesMigration(context: Context) =
        listOf(SharedPreferencesMigration(context, "pref_name"))

    private val dataStore: DataStore<Preferences> = activity._dataStore

    suspend fun saveName(name: String) {
        dataStore.edit { it[NAME_KEY] = name }
    }

    suspend fun getName(): String {
        return dataStore.getValueFlow(NAME_KEY, "").first()
    }

    suspend fun saveHeight(height: Float) {
        dataStore.edit { it[HEIGHT_KEY] = height }
    }

    suspend fun getHeight(): Float {
        return dataStore.getValueFlow(HEIGHT_KEY, 0f).first()
    }

    suspend fun saveWeight(weight: Int) {
        dataStore.edit { it[WEIGHT_KEY] = weight }
    }

    suspend fun getWeight(): Int {
        return dataStore.getValueFlow(WEIGHT_KEY, 0).first()
    }

    var isMale: Boolean
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                dataStore.getValueFlow(IS_MALE_KEY, false).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default) {
                dataStore.edit {
                    it[IS_MALE_KEY] = value
                }
            }
        }

    var isFemale: Boolean
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                dataStore.getValueFlow(IS_FEMALE_KEY, false).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default) {
                dataStore.edit {
                    it[IS_FEMALE_KEY] = value
                }
            }
        }

    suspend fun saveBmi(bmi: Float) {
        dataStore.edit { it[BMI_KEY] = bmi }
    }

    suspend fun getBmi(): Float {
        return dataStore.getValueFlow(BMI_KEY, 0f).first()
    }

    fun getSuggest(bmi: Float): String {
        return when {
            bmi > 24.9 -> "eat less, move more!"
            bmi in 18.5..24.9 -> "perfecto, stay healthy!"
            else -> "are you sick?"
        }
    }
}


fun <T> DataStore<Preferences>.getValueFlow(
    key: Preferences.Key<T>,
    defaultValue: T,
): Flow<T> {
    return this.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: defaultValue
        }
}