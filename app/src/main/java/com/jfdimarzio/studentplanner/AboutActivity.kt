package com.jfdimarzio.studentplanner

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton = findViewById<Button>(R.id.btnBackAbout)

        backButton.setOnClickListener {
            finish()
        }

        applyTheme()
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
    }

    private fun applyTheme() {
        ThemeHelper.applyTheme(
            this,
            findViewById<LinearLayout>(R.id.aboutLayout),
            findViewById<TextView>(R.id.txtAboutTitle),
            listOf(findViewById(R.id.btnBackAbout))
        )
    }
}