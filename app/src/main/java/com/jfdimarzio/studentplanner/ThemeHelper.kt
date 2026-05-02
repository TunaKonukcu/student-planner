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

        val backgroundColor = if (bg == "dark") Color.BLACK else Color.parseColor("#F4F6F8")
        val textColor = if (bg == "dark") Color.WHITE else Color.parseColor("#111111")
        val hintColor = if (bg == "dark") Color.LTGRAY else Color.GRAY

        val buttonColor = when (btn) {
            "green" -> Color.parseColor("#4CAF50")
            "purple" -> Color.parseColor("#7E57C2")
            "orange" -> Color.parseColor("#FF9800")
            else -> Color.parseColor("#2F80ED")
        }

        backgroundView.setBackgroundColor(backgroundColor)
        applyTextColors(backgroundView, textColor, hintColor)

        titleView?.setTextColor(textColor)

        for (button in buttons) {
            button.background.setTint(buttonColor)
            button.setTextColor(Color.WHITE)
        }
    }

    private fun applyTextColors(view: View, textColor: Int, hintColor: Int) {
        when (view) {
            is EditText -> {
                view.setTextColor(textColor)
                view.setHintTextColor(hintColor)
                view.background.setTint(textColor)
            }

            is RadioButton -> {
                view.setTextColor(textColor)
                view.buttonTintList = android.content.res.ColorStateList.valueOf(textColor)
            }

            is Button -> {
                // Buttons handled separately
            }

            is TextView -> {
                view.setTextColor(textColor)
            }

            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    applyTextColors(view.getChildAt(i), textColor, hintColor)
                }
            }
        }
    }
}