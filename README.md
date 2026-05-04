📱 Student Planner App
📌 Project Overview

Student Planner is a mobile Android application designed to help users organize their daily academic and personal tasks. The app allows users to create accounts, manage tasks with date and time, customize the interface with themes, and receive SMS reminders for important tasks.

🚀 Features Implemented
🔐 User Account System
-Users can create an account using a username and password
-User data is stored locally using SharedPreferences
-Each user has their own saved tasks and settings
👤 Profile Management
Users can edit:
-Full Name
-School
-Major
-Phone Number (for SMS reminders)
-Users can select an avatar (👨 or 👩)
-Profile data is saved and loaded per user
🎨 Theme Customization
-Light and Dark background modes
-Multiple button color options:
-Blue
-Green
-Orange
-Purple
Themes apply across:
-Home page
-Profile
-Settings
-To-Do List
-All tasks
-Text colors automatically adjust for readability
✅ Task Management System
Users can:
-Add tasks
-Select date (DatePicker)
-Select time (TimePicker)
-Assign colors to tasks
-Tasks are saved per user
-Tasks persist after app restart
Tasks can be:
-Edited
-Deleted
-Marked as completed
📅 Improved To-Do UI
-Clean, modern design
-Rounded buttons for smooth UI
-Dynamic theme support
-Organized task display
📲 SMS Notification System
-Users can enable/disable SMS reminders
-Each task has its own SMS toggle
-SMS is scheduled using AlarmManager
-SMS is sent at selected date and time
Requires:
-Phone number from profile
-SMS permission
⚙️ Settings System (Upgraded)
-New Settings main page
-Sub-settings include:
-Theme settings
-SMS notification settings
-Settings are saved and applied instantly
🛠️ Technologies Used
Kotlin
Android Studio
XML Layouts
SharedPreferences (local storage)
AlarmManager (task scheduling)
SmsManager (SMS functionality)
