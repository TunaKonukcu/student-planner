package com.jfdimarzio.studentplanner

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity

class SmsNotificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_notification)

        val switchSms = findViewById<Switch>(R.id.switchSms)
        val statusText = findViewById<TextView>(R.id.txtSmsStatus)
        val backButton = findViewById<Button>(R.id.btnBackSms)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val smsEnabled = sharedPref.getBoolean("smsEnabled", false)

        switchSms.isChecked = smsEnabled
        updateStatus(statusText, smsEnabled)

        switchSms.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit()
                .putBoolean("smsEnabled", isChecked)
                .apply()

            updateStatus(statusText, isChecked)
        }

        backButton.setOnClickListener {
            finish()
        }

        applyTheme()
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
    }

    private fun updateStatus(statusText: TextView, enabled: Boolean) {
        if (enabled) {
            statusText.text = "Status: ON ✅"
            statusText.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            statusText.text = "Status: OFF ❌"
            statusText.setTextColor(Color.parseColor("#D32F2F"))
        }
    }

    private fun applyTheme() {
        ThemeHelper.applyTheme(
            this,
            findViewById(R.id.smsLayout),
            findViewById(R.id.txtSmsTitle),
            listOf(findViewById(R.id.btnBackSms))
        )
    }
}