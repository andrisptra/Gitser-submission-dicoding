package com.example.gitser.ui.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModelProvider
import com.example.gitser.R
import com.example.gitser.helper.DataStore
import com.example.gitser.helper.SettingPreferences
import com.example.gitser.ui.main.MainActivity
import com.example.gitser.ui.themeOption.ThemeOptionViewModel
import com.example.gitser.ui.themeOption.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val pref = SettingPreferences.getInstance(application.DataStore)
        val themeOptionViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeOptionViewModel::class.java]

        themeOptionViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val logo = findViewById<ImageView>(R.id.logo)
        logo.alpha = 0F
        logo.animate().setDuration(2000).alpha(1F).withEndAction{
            intentTOMain()
        }

    }

    private fun intentTOMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}