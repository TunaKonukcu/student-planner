package com.jfdimarzio.studentplanner

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity

class ThemeSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_theme_settings)

        val bgGroup = findViewById<RadioGroup>(R.id.bgGroup)
        val buttonGroup = findViewById<RadioGroup>(R.id.buttonGroup)

        val saveButton = findViewById<Button>(R.id.btnSaveSettings)
        val backButton = findViewById<Button>(R.id.btnBackSettings)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        val savedBg = sharedPref.getString("bg", "light")
        val savedBtn = sharedPref.getString("btnColor", "blue")

        if (savedBg == "dark") {
            findViewById<RadioButton>(R.id.bgDark).isChecked = true
        } else {
            findViewById<RadioButton>(R.id.bgLight).isChecked = true
        }

        when (savedBtn) {
            "green" -> findViewById<RadioButton>(R.id.colorGreen).isChecked = true
            "purple" -> findViewById<RadioButton>(R.id.colorPurple).isChecked = true
            "orange" -> findViewById<RadioButton>(R.id.colorOrange).isChecked = true
            else -> findViewById<RadioButton>(R.id.colorBlue).isChecked = true
        }

        applyThemePage()

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

            applyThemePage()

            Toast.makeText(this, "Theme saved", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        applyThemePage()
    }

    private fun applyThemePage() {
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