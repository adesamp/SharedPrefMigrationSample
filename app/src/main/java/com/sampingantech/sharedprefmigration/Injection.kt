package com.sampingantech.sharedprefmigration

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Injection {
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "sampingan_data_store",
        produceMigrations = ::sharedPreferencesMigration
    )

    private fun sharedPreferencesMigration(context: Context) =
        listOf(SharedPreferencesMigration(context, "pref_name"))

    val baseModule = module {
        single { getDataStore(androidApplication()) }
        viewModel { MainViewModel(get()) }
    }

    private fun getDataStore(context: Context) : DataStore<Preferences> = context._dataStore
}