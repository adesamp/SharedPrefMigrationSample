package com.sampingantech.sharedprefmigration

import android.app.Activity
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    var name: String
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(NAME_KEY, "").first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[NAME_KEY] = value
                }
            }
        }


    var height: Float
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(HEIGHT_KEY, 0f).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[HEIGHT_KEY] = value
                }
            }
        }

    var weight: Int
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(WEIGHT_KEY, 0).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[WEIGHT_KEY] = value
                }
            }
        }

    var isMale: Boolean
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(IS_MALE_KEY, false).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[IS_MALE_KEY] = value
                }
            }
        }

    var isFemale: Boolean
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(IS_FEMALE_KEY, false).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[IS_FEMALE_KEY] = value
                }
            }
        }

    var bmi: Float
        get() = runBlocking{
            withContext(Dispatchers.Default){
                dataStore.getValueFlow(BMI_KEY, 0f).first()
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default){
                dataStore.edit {
                    it[BMI_KEY] = value
                }
            }
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