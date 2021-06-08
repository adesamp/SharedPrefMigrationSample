package com.sampingantech.sharedprefmigration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampingantech.sharedprefmigration.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVar()
        clickListener()
    }

    private fun clickListener() = binding.apply {
        btnCalculate.setOnClickListener {
            if (edtWeight.text.toString().isNotBlank() && edtHeight.text.toString().isNotBlank()) {
                val weight = edtWeight.text.toString().toInt()
                val height = edtHeight.text.toString().toFloat()
                val bmi = weight / (height * height)
                val name = edtName.text.toString()
                viewModel.saveName(name)
                viewModel.saveWeight(weight)
                viewModel.saveHeight(height)
                viewModel.saveBmi(bmi)
                viewModel.saveGender(getGender())
            } else {
                edtWeight.error = "input your weight"
                edtHeight.error = "input your height"
            }
        }
    }

    private fun getGender() = when {
        binding.rbMale.isChecked -> BmiBrain.Companion.Gender.Male
        binding.rbFemale.isChecked -> BmiBrain.Companion.Gender.Female
        else -> BmiBrain.Companion.Gender.Unselected
    }

    private fun initVar() {
        viewModel.name.observe(this, ::setName)
        viewModel.height.observe(this, ::setHeight)
        viewModel.weight.observe(this, ::setWeight)
        viewModel.gender.observe(this, ::setGender)
        viewModel.bmiKey.observe(this, ::setBmiKey)
        viewModel.suggest.observe(this, ::setSuggest)
    }

    private fun setName(name: String?) = name?.let { binding.edtName.setText(name) }
    private fun setSuggest(s: String?) = s?.let { binding.tvSuggest.text = s }
    private fun setBmiKey(fl: Float?) = fl?.let { binding.tvBmi.text = String.format("%.2f", fl) }
    private fun setWeight(i: Int?) = i?.let { binding.edtWeight.setText(i.toString()) }
    private fun setHeight(fl: Float?) = fl?.let { binding.edtHeight.setText(fl.toString()) }

    private fun setGender(gender: BmiBrain.Companion.Gender?) = when (gender) {
        BmiBrain.Companion.Gender.Female -> binding.rbFemale.isChecked = true
        BmiBrain.Companion.Gender.Male -> binding.rbMale.isChecked = true
        else -> {
            binding.rbFemale.isChecked = false
            binding.rbFemale.isChecked = false
        }
    }

}