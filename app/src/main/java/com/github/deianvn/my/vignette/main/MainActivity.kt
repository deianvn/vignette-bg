package com.github.deianvn.my.vignette.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.github.deianvn.my.vignette.databinding.ActivityMainBinding
import org.joda.time.LocalDateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel.countries.observe(this) {

        }

        viewModel.vignetteConfig.observe(this) {
            if (it != null) {
                viewModel.loadValidity()
            } else {
                viewModel.clearSavedData()
            }
        }

        viewModel.validity.observe(this) {
            if (it != null) {
                updateTimer(it)
            } else {
                viewModel.requestVignette(

                )
            }

        }

        viewModel.loadConfigurationData(this)
    }

    private fun updateTimer(validity: LocalDateTime) {

    }

}
