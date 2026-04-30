package com.jfdimarzio.studentplanner

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect this Kotlin file to activity_settings.xml
        setContentView(R.layout.activity_settings)

        val bgGroup = findViewById<RadioGroup>(R.id.bgGroup)
        val buttonGroup = findViewById<RadioGroup>(R.id.buttonGroup)

        val saveButton = findViewById<Button>(R.id.btnSaveSettings)
        val backButton = findViewById<Button>(R.id.btnBackSettings)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        // Load saved settings
        val savedBg = sharedPref.getString("bg", "light")
        val savedBtn = sharedPref.getString("btnColor", "blue")

        // Show saved background choice
        if (savedBg == "dark") {
            findViewById<RadioButton>(R.id.bgDark).isChecked = true
        } else {
            findViewById<RadioButton>(R.id.bgLight).isChecked = true
        }

        // Show saved button color choice
        when (savedBtn) {
            "green" -> findViewById<RadioButton>(R.id.colorGreen).isChecked = true
            "purple" -> findViewById<RadioButton>(R.id.colorPurple).isChecked = true
            "orange" -> findViewById<RadioButton>(R.id.colorOrange).isChecked = true
            else -> findViewById<RadioButton>(R.id.colorBlue).isChecked = true
        }

        // Apply saved theme when settings page opens
        applySettingsTheme()

        saveButton.setOnClickListener {
            val bg = if (bgGroup.checkedRadioButtonId == R.id.bgDark) {
                "dark"
            } else {
                "light"
            }

            val btnColor = when (buttonGroup.checkedRadioButtonId) {
                R.id.colorGreen -> "green"
                R.id.colorPurple -> "purple"
                R.id.colorOrange -> "orange"
                else -> "blue"
            }

            sharedPref.edit()
                .putString("bg", bg)
                .putString("btnColor", btnColor)
                .apply()

            // Apply immediately after save
            applySettingsTheme()

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        applySettingsTheme()
    }

    private fun applySettingsTheme() {
        ThemeHelper.applyTheme(
            this,
            findViewById(R.id.settingsScroll),
            null,
            listOf(
                findViewById(R.id.btnSaveSettings),
                findViewById(R.id.btnBackSettings)
            )
        )
    }
}