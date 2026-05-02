package com.jfdimarzio.studentplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {

    private lateinit var settingsMenuLayout: LinearLayout
    private lateinit var settingsTitle: TextView
    private lateinit var btnThemes: Button
    private lateinit var btnSmsNotification: Button
    private lateinit var btnBackSettingsMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsMenuLayout = findViewById(R.id.settingsMenuLayout)
        settingsTitle = findViewById(R.id.txtSettingsMenuTitle)

        btnThemes = findViewById(R.id.btnThemes)
        btnSmsNotification = findViewById(R.id.btnSmsNotification)
        btnBackSettingsMenu = findViewById(R.id.btnBackSettingsMenu)

        btnThemes.setOnClickListener {
            startActivity(Intent(this, ThemeSettingsActivity::class.java))
        }

        btnSmsNotification.setOnClickListener {
            startActivity(Intent(this, SmsNotificationActivity::class.java))
        }

        btnBackSettingsMenu.setOnClickListener {
            finish()
        }

        applySettingsMenuTheme()
    }

    override fun onResume() {
        super.onResume()
        applySettingsMenuTheme()
    }

    private fun applySettingsMenuTheme() {
        ThemeHelper.applyTheme(
            this,
            settingsMenuLayout,
            settingsTitle,
            listOf(btnThemes, btnSmsNotification, btnBackSettingsMenu)
        )
    }
}