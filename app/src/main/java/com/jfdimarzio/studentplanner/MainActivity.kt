package com.jfdimarzio.studentplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var mainLayout: FrameLayout
    private lateinit var titleText: TextView
    private lateinit var welcomeText: TextView
    private lateinit var btnProfile: Button
    private lateinit var btnTodo: Button
    private lateinit var btnSettings: Button
    private lateinit var btnAbout: Button

    private lateinit var btnAllTasks: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLayout)
        titleText = findViewById(R.id.txtTitle)
        welcomeText = findViewById(R.id.txtWelcome)

        btnProfile = findViewById(R.id.btnProfileCircle)
        btnTodo = findViewById(R.id.btnTodo)
        btnSettings = findViewById(R.id.btnSettings)
        btnAbout = findViewById(R.id.btnAbout)
        btnAllTasks = findViewById(R.id.btnAllTasks)


        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnTodo.setOnClickListener {
            startActivity(Intent(this, ToDoActivity::class.java))
        }

        btnAllTasks.setOnClickListener {
            startActivity(Intent(this, AllTasksActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        updateHomeScreen()
    }

    override fun onResume() {
        super.onResume()
        updateHomeScreen()
    }

    private fun updateHomeScreen() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"

        welcomeText.text = "Welcome back, $username!"

        val savedAvatar = sharedPref.getString("avatar_$username", "default")

        btnProfile.text = when (savedAvatar) {
            "man" -> "👨"
            "woman" -> "👩"
            else -> "👤"
        }

        ThemeHelper.applyTheme(
            this,
            mainLayout,
            titleText,
            listOf(btnProfile, btnTodo, btnAllTasks, btnSettings, btnAbout)
        )
    }
}