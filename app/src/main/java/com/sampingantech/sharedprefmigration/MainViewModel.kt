package com.sampingantech.sharedprefmigration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.BMI_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.HEIGHT_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.IS_FEMALE_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.IS_MALE_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.NAME_KEY
import com.sampingantech.sharedprefmigration.BmiBrain.Companion.WEIGHT_KEY
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name
    private val _height = MutableLiveData<Float>()
    val height: LiveData<Float>
        get() = _height
    private val _weight = MutableLiveData<Int>()
    val weight: LiveData<Int>
        get() = _weight
    private val _gender = MutableLiveData<BmiBrain.Companion.Gender>()
    val gender: LiveData<BmiBrain.Companion.Gender>
        get() = _gender
    private val _bmiKey = MutableLiveData<Float>()
    val bmiKey: LiveData<Float>
        get() = _bmiKey
    private val _suggest = MutableLiveData<String>()
    val suggest: LiveData<String>
        get() = _suggest

    fun fetch() {
        viewModelScope.launch {
            dataStore.data
                .catch { _name.postValue(it.message) }
                .map {
                    _name.postValue(it[NAME_KEY])
                    _height.postValue(it[HEIGHT_KEY])
                    _weight.postValue(it[WEIGHT_KEY])
                    _bmiKey.postValue(it[BMI_KEY])
                    saveSuggest(it[BMI_KEY])
                    when {
                        it[IS_MALE_KEY] == true -> {
                            _gender.postValue(BmiBrain.Companion.Gender.Male)
                        }
                        it[IS_FEMALE_KEY] == true -> {
                            _gender.postValue(BmiBrain.Companion.Gender.Female)
                        }
                        else -> _gender.postValue(BmiBrain.Companion.Gender.Unselected)
                    }
                }
                .first()
        }
    }

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
            _name.postValue(name)
        }
    }

    fun saveWeight(weight: Int) {
        viewModelScope.launch {
            dataStore.edit { it[WEIGHT_KEY] = weight }
            _weight.postValue(weight)
        }
    }

    fun saveHeight(height: Float) {
        viewModelScope.launch {
            dataStore.edit { it[HEIGHT_KEY] = height }
            _height.postValue(height)
        }
    }

    fun saveBmi(bmi: Float) {
        viewModelScope.launch {
            dataStore.edit {
                it[BMI_KEY] = bmi
                saveSuggest(bmi)
            }
            _bmiKey.postValue(bmi)
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
                _gender.postValue(gender)
            }
        }
    }
}