package com.jfdimarzio.studentplanner

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class AllTasksActivity : ComponentActivity() {

    private lateinit var taskContainer: LinearLayout
    private var tasks = mutableListOf<String>()
    private lateinit var taskKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tasks)

        taskContainer = findViewById(R.id.allTasksContainer)
        val backButton = findViewById<Button>(R.id.btnBackAllTasks)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"
        taskKey = "tasks_$username"

        loadTasks()
        showTasks()

        backButton.setOnClickListener {
            finish()
        }

        applyTheme()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
        showTasks()
        applyTheme()
    }

    private fun loadTasks() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val savedTasks = sharedPref.getString(taskKey, "") ?: ""

        tasks = if (savedTasks.isNotEmpty()) {
            savedTasks.split(";;").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveTasks() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        sharedPref.edit()
            .putString(taskKey, tasks.joinToString(";;"))
            .apply()
    }

    private fun showTasks() {
        taskContainer.removeAllViews()

        if (tasks.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "No tasks saved yet."
            emptyText.textSize = 18f
            taskContainer.addView(emptyText)
            return
        }

        for (i in tasks.indices) {
            val parts = tasks[i].split("|")
            if (parts.size < 5) continue

            val taskText = parts[0]
            val date = parts[1]
            val time = parts[2]
            val colorName = parts[3]
            val completed = parts[4].toBoolean()
            val smsChoice = if (parts.size >= 6) parts[5].toBoolean() else false

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(20, 20, 20, 20)

            val cardColor = when (colorName) {
                "green" -> Color.parseColor("#DFF5E1")
                "orange" -> Color.parseColor("#FFE5C2")
                "purple" -> Color.parseColor("#E8D9FF")
                else -> Color.parseColor("#DCEBFF")
            }

            card.setBackgroundColor(cardColor)

            val checkBox = CheckBox(this)
            checkBox.text = "$taskText\n$date at $time\nSMS: ${if (smsChoice) "ON ✅" else "OFF ❌"}"
            checkBox.textSize = 18f
            checkBox.setTextColor(Color.BLACK)
            checkBox.isChecked = completed

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                tasks[i] = "$taskText|$date|$time|$colorName|$isChecked|$smsChoice"
                saveTasks()
            }

            val deleteButton = Button(this)
            deleteButton.text = "Remove"
            deleteButton.setOnClickListener {
                tasks.removeAt(i)
                saveTasks()
                showTasks()
            }

            card.addView(checkBox)
            card.addView(deleteButton)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 18)
            card.layoutParams = params

            taskContainer.addView(card)
        }
    }

    private fun applyTheme() {
        ThemeHelper.applyTheme(
            this,
            findViewById(R.id.allTasksScroll),
            findViewById(R.id.txtAllTasksTitle),
            listOf(findViewById(R.id.btnBackAllTasks))
        )
    }
}