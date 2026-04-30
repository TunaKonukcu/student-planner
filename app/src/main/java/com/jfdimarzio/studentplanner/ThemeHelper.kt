package com.jfdimarzio.studentplanner

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

object ThemeHelper {

    fun applyTheme(
        activity: Activity,
        backgroundView: View,
        titleView: TextView?,
        buttons: List<Button>
    ) {
        val sharedPref = activity.getSharedPreferences("UserData", Activity.MODE_PRIVATE)

        val bg = sharedPref.getString("bg", "light")
        val btn = sharedPref.getString("btnColor", "blue")

        val backgroundColor: Int
        val textColor: Int
        val hintColor: Int

        if (bg == "dark") {
            backgroundColor = Color.BLACK
            textColor = Color.WHITE
            hintColor = Color.LTGRAY
        } else {
            backgroundColor = Color.parseColor("#F4F6F8")
            textColor = Color.parseColor("#111111")
            hintColor = Color.GRAY
        }

        val buttonColor = when (btn) {
            "green" -> Color.parseColor("#4CAF50")
            "purple" -> Color.parseColor("#7E57C2")
            "orange" -> Color.parseColor("#FF9800")
            else -> Color.parseColor("#2F80ED")
        }

        backgroundView.setBackgroundColor(backgroundColor)

        applyTextColorRecursively(backgroundView, textColor, hintColor)

        titleView?.setTextColor(textColor)

        for (button in buttons) {
            button.background.setTint(buttonColor)
            button.setTextColor(Color.WHITE)
        }
    }

    private fun applyTextColorRecursively(view: View, textColor: Int, hintColor: Int) {
        when (view) {
            is EditText -> {
                view.setTextColor(textColor)
                view.setHintTextColor(hintColor)
                view.setBackgroundColor(Color.TRANSPARENT)
            }

            is RadioButton -> {
                view.setTextColor(textColor)
            }

            is Button -> {
                // Do nothing here because button colors are handled separately
            }

            is TextView -> {
                view.setTextColor(textColor)
            }

            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    applyTextColorRecursively(view.getChildAt(i), textColor, hintColor)
                }
            }
        }
    }
}