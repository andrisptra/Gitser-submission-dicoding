package com.example.gitser.ui.themeOption

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.gitser.databinding.ActivityThemeOptionBinding
import com.example.gitser.helper.DataStore
import com.example.gitser.helper.SettingPreferences

class ThemeOptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.DataStore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ThemeOptionViewModel::class.java
        )
        val switchTheme = binding.switchTheme
        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }
}