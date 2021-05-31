package com.sampingantech.sharedprefmigration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sampingantech.sharedprefmigration.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bmiBrain: BmiBrain

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
                val isMale = rbMale.isChecked
                val isFemale = rbFemale.isChecked

                tvBmi.text = String.format("%.2f", bmi)
                tvSuggest.text = bmiBrain.getSuggest(bmi)

                lifecycleScope.launch {
                    bmiBrain.saveName(name)
                    bmiBrain.saveHeight(height)
                    bmiBrain.saveWeight(weight)
                    bmiBrain.saveBmi(bmi)
                    bmiBrain.saveGender(isMale, isFemale)
                }
            } else {
                edtWeight.error = "input your weight"
                edtHeight.error = "input your height"
            }
        }
    }

    private fun initVar() {
        bmiBrain = BmiBrain(this)
        binding.apply {
            lifecycleScope.launch {
                edtName.setText(bmiBrain.getName())
                bmiBrain.getHeight().let {
                    if (it != 0F) edtHeight.setText(it.toString())
                }
                bmiBrain.getWeight().let {
                    if (it != 0) edtWeight.setText(it.toString())
                }
                bmiBrain.getBmi().let {
                    if (it != 0F) {
                        tvBmi.text = it.toString()
                        tvSuggest.text = bmiBrain.getSuggest(it)
                    }
                }
                rbMale.isChecked = bmiBrain.isMale()
                rbFemale.isChecked = bmiBrain.isFemale()
            }
        }
    }
}