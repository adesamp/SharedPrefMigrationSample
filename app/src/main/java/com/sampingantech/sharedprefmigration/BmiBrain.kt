package com.sampingantech.sharedprefmigration

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class BmiBrain {
    companion object {
        val NAME_KEY = stringPreferencesKey("NAME_KEY")
        val HEIGHT_KEY = floatPreferencesKey("HEIGHT_KEY")
        val WEIGHT_KEY = intPreferencesKey("WEIGHT_KEY")
        val IS_MALE_KEY = booleanPreferencesKey("IS_MALE_KEY")
        val IS_FEMALE_KEY = booleanPreferencesKey("IS_FEMALE_KEY")
        val BMI_KEY = floatPreferencesKey("BMI_KEY")

        sealed class Gender {
            object Male : Gender()
            object Female : Gender()
            object Unselected : Gender()
        }
    }
}