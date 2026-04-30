package com.jfdimarzio.studentplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import java.util.Calendar

class ToDoActivity : ComponentActivity() {

    private lateinit var taskInput: EditText
    private lateinit var taskContainer: LinearLayout
    private lateinit var pickDateButton: Button
    private lateinit var pickTimeButton: Button
    private lateinit var colorGroup: RadioGroup

    private var selectedDate = "No date"
    private var selectedTime = "No time"
    private var tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        taskInput = findViewById(R.id.editTask)
        taskContainer = findViewById(R.id.taskContainer)
        pickDateButton = findViewById(R.id.btnPickDate)
        pickTimeButton = findViewById(R.id.btnPickTime)
        colorGroup = findViewById(R.id.colorGroup)

        val addButton = findViewById<Button>(R.id.btnAddTask)
        val backButton = findViewById<Button>(R.id.btnBackTodo)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"
        val taskKey = "tasks_$username"

        val savedTasks = sharedPref.getString(taskKey, "") ?: ""
        if (savedTasks.isNotEmpty()) {
            tasks = savedTasks.split(";;").toMutableList()
        }

        applySavedTheme()
        showTasks(taskKey)

        pickDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDate = "${month + 1}/$day/$year"
                    pickDateButton.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        pickTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            TimePickerDialog(
                this,
                { _, hour, minute ->
                    selectedTime = String.format("%02d:%02d", hour, minute)
                    pickTimeButton.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        addButton.setOnClickListener {
            val taskText = taskInput.text.toString()

            if (taskText.isNotEmpty()) {
                val colorName = when (colorGroup.checkedRadioButtonId) {
                    R.id.colorGreen -> "green"
                    R.id.colorOrange -> "orange"
                    R.id.colorPurple -> "purple"
                    else -> "blue"
                }

                val newTask = "$taskText|$selectedDate|$selectedTime|$colorName|false"
                tasks.add(newTask)

                saveTasks(taskKey)
                showTasks(taskKey)

                taskInput.text.clear()
                selectedDate = "No date"
                selectedTime = "No time"
                pickDateButton.text = "Choose Date"
                pickTimeButton.text = "Choose Time"
            } else {
                Toast.makeText(this, "Enter a task first", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"
        val taskKey = "tasks_$username"

        val savedTasks = sharedPref.getString(taskKey, "") ?: ""

        tasks = if (savedTasks.isNotEmpty()) {
            savedTasks.split(";;").toMutableList()
        } else {
            mutableListOf()
        }

        applySavedTheme()
        showTasks(taskKey)
    }

    private fun showTasks(taskKey: String) {
        taskContainer.removeAllViews()

        for (i in tasks.indices) {
            val parts = tasks[i].split("|")

            if (parts.size < 5) {
                continue
            }

            val taskText = parts[0]
            val date = parts[1]
            val time = parts[2]
            val colorName = parts[3]
            val completed = parts[4].toBoolean()

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
            checkBox.text = "$taskText\n$date at $time"
            checkBox.textSize = 18f
            checkBox.isChecked = completed
            checkBox.setTextColor(Color.parseColor("#222222"))

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                tasks[i] = "$taskText|$date|$time|$colorName|$isChecked"
                saveTasks(taskKey)
            }

            val editButton = Button(this)
            editButton.text = "Edit"

            editButton.setOnClickListener {
                taskInput.setText(taskText)
                tasks.removeAt(i)
                saveTasks(taskKey)
                showTasks(taskKey)
            }

            val deleteButton = Button(this)
            deleteButton.text = "Remove"

            deleteButton.setOnClickListener {
                tasks.removeAt(i)
                saveTasks(taskKey)
                showTasks(taskKey)
            }

            card.addView(checkBox)
            card.addView(editButton)
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

    private fun saveTasks(taskKey: String) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        sharedPref.edit()
            .putString(taskKey, tasks.joinToString(";;"))
            .apply()
    }

    private fun applySavedTheme() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        val bg = sharedPref.getString("bg", "light")
        val btn = sharedPref.getString("btnColor", "blue")

        val backgroundColor =
            if (bg == "dark") Color.BLACK
            else Color.parseColor("#F4F6F8")

        val textColor =
            if (bg == "dark") Color.WHITE
            else Color.parseColor("#111111")

        val hintColor =
            if (bg == "dark") Color.LTGRAY
            else Color.GRAY

        val buttonColor = when (btn) {
            "green" -> Color.parseColor("#4CAF50")
            "purple" -> Color.parseColor("#7E57C2")
            "orange" -> Color.parseColor("#FF9800")
            else -> Color.parseColor("#2F80ED")
        }

        val todoScroll = findViewById<ScrollView>(R.id.todoScroll)
        val todoLayout = findViewById<LinearLayout>(R.id.todoLayout)
        val todoTitle = findViewById<TextView>(R.id.txtTodoTitle)
        val addButton = findViewById<Button>(R.id.btnAddTask)
        val backButton = findViewById<Button>(R.id.btnBackTodo)

        todoScroll.setBackgroundColor(backgroundColor)
        todoLayout.setBackgroundColor(backgroundColor)
        todoTitle.setTextColor(textColor)

        taskInput.setTextColor(textColor)
        taskInput.setHintTextColor(hintColor)
        taskInput.background.setTint(textColor)

        pickDateButton.background.setTint(buttonColor)
        pickTimeButton.background.setTint(buttonColor)
        addButton.background.setTint(buttonColor)
        backButton.background.setTint(buttonColor)

        pickDateButton.setTextColor(Color.WHITE)
        pickTimeButton.setTextColor(Color.WHITE)
        addButton.setTextColor(Color.WHITE)
        backButton.setTextColor(Color.WHITE)
    }
}