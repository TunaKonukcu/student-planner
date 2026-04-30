package com.jfdimarzio.studentplanner

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileLayout = findViewById<LinearLayout>(R.id.profileLayout)
        val title = findViewById<TextView>(R.id.txtProfileTitle)
        val avatarText = findViewById<TextView>(R.id.txtAvatar)
        val avatarGroup = findViewById<RadioGroup>(R.id.avatarGroup)

        val fullNameInput = findViewById<EditText>(R.id.editFullName)
        val schoolInput = findViewById<EditText>(R.id.editSchool)
        val majorInput = findViewById<EditText>(R.id.editMajor)

        val savedUserText = findViewById<TextView>(R.id.txtSavedUser)
        val saveButton = findViewById<Button>(R.id.btnSaveProfile)
        val backButton = findViewById<Button>(R.id.btnBackProfile)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"

        savedUserText.text = "Username: $username"

        val fullNameKey = "fullName_$username"
        val schoolKey = "school_$username"
        val majorKey = "major_$username"
        val avatarKey = "avatar_$username"

        fullNameInput.setText(sharedPref.getString(fullNameKey, ""))
        schoolInput.setText(sharedPref.getString(schoolKey, ""))
        majorInput.setText(sharedPref.getString(majorKey, ""))

        val savedAvatar = sharedPref.getString(avatarKey, "man")

        when (savedAvatar) {
            "man" -> {
                avatarText.text = "👨"
                findViewById<RadioButton>(R.id.avatarMan).isChecked = true
            }
            "woman" -> {
                avatarText.text = "👩"
                findViewById<RadioButton>(R.id.avatarWoman).isChecked = true
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
                R.id.avatarMan -> "man"
                R.id.avatarWoman -> "woman"
                else -> "man"
            }

            sharedPref.edit()
                .putString(fullNameKey, fullNameInput.text.toString())
                .putString(schoolKey, schoolInput.text.toString())
                .putString(majorKey, majorInput.text.toString())
                .putString(avatarKey, selectedAvatar)
                .apply()

            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }

        val savedTheme = sharedPref.getString("theme", "blue")
        val backgroundColor: Int
        val primaryColor: Int
        val buttonColor: Int

        when (savedTheme) {
            "dark" -> {
                backgroundColor = Color.parseColor("#101820")
                primaryColor = Color.parseColor("#F2AA4C")
                buttonColor = Color.parseColor("#F2AA4C")
                savedUserText.setTextColor(Color.WHITE)
            }
            "green" -> {
                backgroundColor = Color.parseColor("#F3F7F0")
                primaryColor = Color.parseColor("#2D5A27")
                buttonColor = Color.parseColor("#4CAF50")
            }
            "purple" -> {
                backgroundColor = Color.parseColor("#F6F0FF")
                primaryColor = Color.parseColor("#4B2E83")
                buttonColor = Color.parseColor("#7E57C2")
            }
            else -> {
                backgroundColor = Color.parseColor("#F4F6F8")
                primaryColor = Color.parseColor("#1E3A5F")
                buttonColor = Color.parseColor("#2F80ED")
            }
        }

        profileLayout.setBackgroundColor(backgroundColor)
        title.setTextColor(primaryColor)
        saveButton.setBackgroundColor(buttonColor)
        backButton.setBackgroundColor(buttonColor)
        saveButton.setTextColor(Color.WHITE)
        backButton.setTextColor(Color.WHITE)
    }
}