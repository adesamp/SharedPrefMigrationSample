package com.sampingantech.sharedprefmigration

import android.app.Activity
import android.content.Context

class BmiBrain(activity: Activity) {

    companion object {
        private const val NAME_KEY = "NAME_KEY"
        private const val HEIGHT_KEY = "HEIGHT_KEY"
        private const val WEIGHT_KEY = "WEIGHT_KEY"
        private const val IS_MALE_KEY = "IS_MALE_KEY"
        private const val IS_FEMALE_KEY = "IS_FEMALE_KEY"
        private const val BMI_KEY = "BMI_KEY"
    }

    private val preferences = activity.getSharedPreferences("pref_name", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    var name: String
        get() {
            return preferences.getString(NAME_KEY, "").orEmpty()
        }
        set(value) {
            editor.putString(NAME_KEY, value)
            editor.commit()
        }

    var height: Float
        get() {
            return preferences.getFloat(HEIGHT_KEY, 0F)
        }
        set(value) {
            editor.putFloat(HEIGHT_KEY, value)
            editor.commit()
        }

    var weight: Int
        get() {
            return preferences.getInt(WEIGHT_KEY, 0)
        }
        set(value) {
            editor.putInt(WEIGHT_KEY, value)
            editor.commit()
        }

    var isMale: Boolean
        get() {
            return preferences.getBoolean(IS_MALE_KEY, false)
        }
        set(value) {
            editor.putBoolean(IS_MALE_KEY, value)
            editor.commit()
        }

    var isFemale: Boolean
        get() {
            return preferences.getBoolean(IS_FEMALE_KEY, false)
        }
        set(value) {
            editor.putBoolean(IS_FEMALE_KEY, value)
            editor.commit()
        }

    var bmi: Float
        get() {
            return preferences.getFloat(BMI_KEY, 0F)
        }
        set(value) {
            editor.putFloat(BMI_KEY, value)
            editor.commit()
        }

    fun getSuggest(bmi: Float): String {
        return when {
            bmi > 24.9 -> "eat less, move more!"
            bmi in 18.5..24.9 -> "perfecto, stay healthy!"
            else -> "are you sick?"
        }
    }
}