📱 Student Planner App
📌 Project Overview

Student Planner is a mobile Android application designed to help users organize their daily academic and personal tasks. The application allows users to create accounts, manage tasks with specific dates and times, customize the interface through themes, and receive SMS reminders for important events.

🚀 Features
🔐 User Account System
Users can create an account using a username and password
Data is stored locally using SharedPreferences
Each user has personalized data (tasks, settings, profile)
👤 Profile Management
Users can update:
Full Name
School
Major
Phone Number (for SMS reminders)
Avatar selection (Male or Female)
Profile data is saved and loaded per user
🎨 Theme Customization
Light and Dark background modes
Multiple button color options:
Blue
Green
Orange
Purple
Themes apply across all screens:
Home
Profile
Settings
To-Do List
Text automatically adjusts for readability
✅ Task Management
Add tasks with:
Date (DatePicker)
Time (TimePicker)
Custom color labels
Tasks are:
Saved per user
Persistent after restart
Editable and removable
Markable as completed
📲 SMS Notification System
Users can enable/disable SMS notifications
Each task has its own SMS toggle
SMS reminders are scheduled using AlarmManager
SMS is sent at the selected date and time

Requirements:

Valid phone number in profile
SMS permission enabled
Works best on a real device with SIM card
⚙️ Settings System
Dedicated settings page
Includes:
Theme settings
SMS notification settings
Changes are saved and applied immediately
🎯 UI Improvements
Clean and modern layout
Rounded buttons for smooth design
Improved spacing and readability
Consistent styling across all pages
🛠️ Technologies Used
Kotlin
Android Studio
XML Layouts
SharedPreferences (local storage)
AlarmManager (task scheduling)
SmsManager (SMS functionality)
