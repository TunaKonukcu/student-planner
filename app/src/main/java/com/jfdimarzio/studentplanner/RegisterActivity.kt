package com.jfdimarzio.studentplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameInput = findViewById<EditText>(R.id.editNewUsername)
        val passwordInput = findViewById<EditText>(R.id.editNewPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                sharedPref.edit()
                    .putString("username", username)
                    .putString("password", password)
                    .apply()

                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}