package com.jfdimarzio.studentplanner

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val avatarText = findViewById<TextView>(R.id.txtAvatar)
        val avatarGroup = findViewById<RadioGroup>(R.id.avatarGroup)

        val fullNameInput = findViewById<EditText>(R.id.editFullName)
        val phoneInput = findViewById<EditText>(R.id.editPhoneNumber)
        val schoolInput = findViewById<EditText>(R.id.editSchool)
        val majorInput = findViewById<EditText>(R.id.editMajor)

        val savedUserText = findViewById<TextView>(R.id.txtSavedUser)
        val saveButton = findViewById<Button>(R.id.btnSaveProfile)
        val backButton = findViewById<Button>(R.id.btnBackProfile)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"

        savedUserText.text = "Username: $username"

        val fullNameKey = "fullName_$username"
        val phoneKey = "phone_$username"
        val schoolKey = "school_$username"
        val majorKey = "major_$username"
        val avatarKey = "avatar_$username"

        fullNameInput.setText(sharedPref.getString(fullNameKey, ""))
        phoneInput.setText(sharedPref.getString(phoneKey, ""))
        schoolInput.setText(sharedPref.getString(schoolKey, ""))
        majorInput.setText(sharedPref.getString(majorKey, ""))

        val savedAvatar = sharedPref.getString(avatarKey, "man")

        when (savedAvatar) {
            "woman" -> {
                avatarText.text = "👩"
                findViewById<RadioButton>(R.id.avatarWoman).isChecked = true
            }
            else -> {
                avatarText.text = "👨"
                findViewById<RadioButton>(R.id.avatarMan).isChecked = true
            }
        }

        avatarGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.avatarMan -> avatarText.text = "👨"
                R.id.avatarWoman -> avatarText.text = "👩"
            }
        }

        saveButton.setOnClickListener {
            val selectedAvatar = when (avatarGroup.checkedRadioButtonId) {
                R.id.avatarWoman -> "woman"
                else -> "man"
            }

            sharedPref.edit()
                .putString(fullNameKey, fullNameInput.text.toString())
                .putString(phoneKey, phoneInput.text.toString())
                .putString(schoolKey, schoolInput.text.toString())
                .putString(majorKey, majorInput.text.toString())
                .putString(avatarKey, selectedAvatar)
                .apply()

            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }

        applyProfileTheme()
    }

    override fun onResume() {
        super.onResume()
        applyProfileTheme()
    }

    private fun applyProfileTheme() {
        ThemeHelper.applyTheme(
            this,
            findViewById(R.id.profileScroll),
            findViewById(R.id.txtProfileTitle),
            listOf(
                findViewById(R.id.btnSaveProfile),
                findViewById(R.id.btnBackProfile)
            )
        )
    }
}