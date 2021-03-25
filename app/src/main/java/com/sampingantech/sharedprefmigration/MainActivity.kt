package com.sampingantech.sharedprefmigration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampingantech.sharedprefmigration.databinding.ActivityMainBinding

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

                bmiBrain.name = edtName.text.toString()
                bmiBrain.height = height
                bmiBrain.weight = weight
                bmiBrain.isMale = rbMale.isChecked
                bmiBrain.isFemale = rbFemale.isChecked
                bmiBrain.bmi = bmi

                tvBmi.text = String.format("%.2f", bmi)
                tvSuggest.text = bmiBrain.getSuggest(bmi)
            } else {
                edtWeight.error = "input your weight"
                edtHeight.error = "input your height"
            }
        }
    }

    private fun initVar() {
        bmiBrain = BmiBrain(this)
        binding.apply {
            edtName.setText(bmiBrain.name)
            bmiBrain.height.let {
                if (it != 0F) edtHeight.setText(it.toString())
            }
            bmiBrain.weight.let {
                if (it != 0) edtWeight.setText(it.toString())
            }
            rbMale.isChecked = bmiBrain.isMale
            rbFemale.isChecked = bmiBrain.isFemale
            bmiBrain.bmi.let {
                if (it != 0F) {
                    tvBmi.text = it.toString()
                    tvSuggest.text = bmiBrain.getSuggest(it)
                }
            }
        }
    }
}