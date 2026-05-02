package com.jfdimarzio.studentplanner

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import java.util.Calendar

class ToDoActivity : ComponentActivity() {

    private lateinit var taskInput: EditText
    private lateinit var pickDateButton: Button
    private lateinit var pickTimeButton: Button
    private lateinit var colorGroup: RadioGroup
    private lateinit var checkSmsTask: Switch
    private lateinit var smsTaskStatus: TextView

    private var selectedDate = "No date"
    private var selectedTime = "No time"
    private var tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 100)
        }

        taskInput = findViewById(R.id.editTask)
        pickDateButton = findViewById(R.id.btnPickDate)
        pickTimeButton = findViewById(R.id.btnPickTime)
        colorGroup = findViewById(R.id.colorGroup)
        checkSmsTask = findViewById(R.id.checkSmsTask)
        smsTaskStatus = findViewById(R.id.txtSmsTaskStatus)

        val addButton = findViewById<Button>(R.id.btnAddTask)
        val backButton = findViewById<Button>(R.id.btnBackTodo)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"
        val taskKey = "tasks_$username"

        loadTasks(taskKey)
        applySavedTheme()

        checkSmsTask.setOnCheckedChangeListener { _, isChecked ->
            updateSmsStatus(isChecked)
        }

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

                val smsChoice = checkSmsTask.isChecked
                val newTask = "$taskText|$selectedDate|$selectedTime|$colorName|false|$smsChoice"

                tasks.add(newTask)
                saveTasks(taskKey)

                if (smsChoice) {
                    scheduleSmsReminder(taskText)
                }

                taskInput.text.clear()
                selectedDate = "No date"
                selectedTime = "No time"
                pickDateButton.text = "Choose Date"
                pickTimeButton.text = "Choose Time"

                checkSmsTask.isChecked = false
                updateSmsStatus(false)

                Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show()
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

        loadTasks(taskKey)
        applySavedTheme()
    }

    private fun loadTasks(taskKey: String) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val savedTasks = sharedPref.getString(taskKey, "") ?: ""

        tasks = if (savedTasks.isNotEmpty()) {
            savedTasks.split(";;").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveTasks(taskKey: String) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        sharedPref.edit()
            .putString(taskKey, tasks.joinToString(";;"))
            .apply()
    }

    private fun updateSmsStatus(isEnabled: Boolean) {
        if (isEnabled) {
            smsTaskStatus.text = "SMS ON ✅"
            smsTaskStatus.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            smsTaskStatus.text = "SMS OFF ❌"
            smsTaskStatus.setTextColor(Color.parseColor("#D32F2F"))
        }
    }

    private fun scheduleSmsReminder(taskText: String) {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        val smsEnabled = sharedPref.getBoolean("smsEnabled", false)
        val username = sharedPref.getString("username", "User") ?: "User"
        val phoneNumber = sharedPref.getString("phone_$username", "") ?: ""

        if (!smsEnabled) {
            Toast.makeText(this, "SMS notifications are off in Settings", Toast.LENGTH_SHORT).show()
            return
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Add phone number in Profile first", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDate == "No date" || selectedTime == "No time") {
            Toast.makeText(this, "Choose date and time for SMS", Toast.LENGTH_SHORT).show()
            return
        }

        val dateParts = selectedDate.split("/")
        val timeParts = selectedTime.split(":")

        val month = dateParts[0].toInt() - 1
        val day = dateParts[1].toInt()
        val year = dateParts[2].toInt()
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            Toast.makeText(this, "Choose a future time for SMS", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, SmsReminderReceiver::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("taskText", taskText)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(this, "SMS reminder scheduled", Toast.LENGTH_SHORT).show()
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
        val timeLabel = findViewById<TextView>(R.id.txtTimeLabel)

        val addButton = findViewById<Button>(R.id.btnAddTask)
        val backButton = findViewById<Button>(R.id.btnBackTodo)

        val smsChoiceLayout = findViewById<LinearLayout>(R.id.smsChoiceLayout)
        val smsTitle = findViewById<TextView>(R.id.txtSmsReminderTitle)
        val smsInfo = findViewById<TextView>(R.id.txtSmsReminderInfo)

        todoScroll.setBackgroundColor(backgroundColor)
        todoLayout.setBackgroundColor(backgroundColor)

        todoTitle.setTextColor(textColor)
        timeLabel.setTextColor(textColor)

        taskInput.setTextColor(textColor)
        taskInput.setHintTextColor(hintColor)
        taskInput.background.setTint(textColor)

        colorGroup.setBackgroundColor(backgroundColor)

        for (i in 0 until colorGroup.childCount) {
            val radioButton = colorGroup.getChildAt(i) as RadioButton
            radioButton.setTextColor(textColor)
            radioButton.buttonTintList = android.content.res.ColorStateList.valueOf(textColor)
        }

        smsChoiceLayout.setBackgroundColor(backgroundColor)
        smsTitle.setTextColor(textColor)
        smsInfo.setTextColor(textColor)
        checkSmsTask.setTextColor(textColor)

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