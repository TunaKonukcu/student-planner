package com.jfdimarzio.studentplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager

class SmsReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: return
        val taskText = intent.getStringExtra("taskText") ?: "Task reminder"

        val message = "Student Planner Reminder: $taskText"

        SmsManager.getDefault().sendTextMessage(
            phoneNumber,
            null,
            message,
            null,
            null
        )
    }
}