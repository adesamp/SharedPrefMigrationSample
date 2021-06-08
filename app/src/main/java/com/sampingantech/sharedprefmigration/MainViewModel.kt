package com.sampingantech.sharedprefmigration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.BMI_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.HEIGHT_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.IS_FEMALE_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.IS_MALE_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.NAME_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.WEIGHT_KEY
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    val name = dataStore.data.map {
        it[NAME_KEY]
    }.asLiveData()

    val height = dataStore.data.map {
        it[HEIGHT_KEY]
    }.asLiveData()

    val weight = dataStore.data.map {
        it[WEIGHT_KEY]
    }.asLiveData()

    val gender = dataStore.data.map {
        when {
            it[IS_MALE_KEY] == true -> {
                BmiBrain.Companion.Gender.Male
            }
            it[IS_FEMALE_KEY] == true -> {
                BmiBrain.Companion.Gender.Female
            }
            else -> BmiBrain.Companion.Gender.Unselected
        }
    }.asLiveData()

    val bmiKey = dataStore.data.map {
        saveSuggest(it[BMI_KEY])
        it[BMI_KEY]
    }.asLiveData()

    private val _suggest = MutableLiveData<String>()
    val suggest: LiveData<String>
        get() = _suggest

    private fun saveSuggest(bmiData: Float?) {
        val bmi = bmiData ?: 0f
        val suggest = when {
            bmi > 24.9 -> "eat less, move more!"
            bmi in 18.5..24.9 -> "perfecto, stay healthy!"
            else -> "are you sick?"
        }
        _suggest.postValue(suggest)
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            dataStore.edit { it[NAME_KEY] = name }
        }
    }

    fun saveWeight(weight: Int) {
        viewModelScope.launch {
            dataStore.edit { it[WEIGHT_KEY] = weight }
        }
    }

    fun saveHeight(height: Float) {
        viewModelScope.launch {
            dataStore.edit { it[HEIGHT_KEY] = height }
        }
    }

    fun saveBmi(bmi: Float) {
        viewModelScope.launch {
            dataStore.edit {
                it[BMI_KEY] = bmi
                saveSuggest(bmi)
            }
        }
    }

    fun saveGender(gender: BmiBrain.Companion.Gender) {
        viewModelScope.launch {
            dataStore.edit {
                when (gender) {
                    BmiBrain.Companion.Gender.Female -> {
                        it[IS_FEMALE_KEY] = true
                        it[IS_MALE_KEY] = false
                    }
                    BmiBrain.Companion.Gender.Male -> {
                        it[IS_FEMALE_KEY] = false
                        it[IS_MALE_KEY] = true
                    }
                    BmiBrain.Companion.Gender.Unselected -> {
                        it[IS_FEMALE_KEY] = false
                        it[IS_MALE_KEY] = false
                    }
                }
            }
        }
    }
}