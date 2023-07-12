package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateConverter {
    fun convertMillsToString(timeMills: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMills
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}