package com.example.timesheet

import android.content.Context
import android.os.Build
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Utils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDateRangeText(context: Context, textView: TextView, startDate: LocalDate, buttons: List<Button>) {
        val formatterDate = DateTimeFormatter.ofPattern("d")
        buttons.forEachIndexed { index, button ->
            button.text = startDate.plusDays(index.toLong()).format(formatterDate)
        }

        val endDate = startDate.plusDays(6)
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val startOfWeekFormatted = startDate.format(formatter)
        val endOfWeekFormatted = endDate.format(formatter)
        val dateRangeText = "$startOfWeekFormatted - $endOfWeekFormatted"
        textView.text = dateRangeText
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initializeDateButtons(context: Context, currentWeekStartDate: LocalDate, buttons: List<Button>, onSelectDate: (LocalDate) -> Unit) {
        val formatterDate = DateTimeFormatter.ofPattern("d")
        buttons.forEachIndexed { index, button ->
            button.text = currentWeekStartDate.plusDays(index.toLong()).format(formatterDate)
            button.setOnClickListener {
                 selectDate(context, button, buttons, currentWeekStartDate.plusDays(index.toLong()), onSelectDate)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun highlightTodayButton(context: Context, buttons: List<Button>, currentWeekStartDate: LocalDate):LocalDate {
        val today = LocalDate.now()
        val todayIndex = currentWeekStartDate.until(today, ChronoUnit.DAYS).toInt()
        if (todayIndex in buttons.indices) {
            val todayButton = buttons[todayIndex]
            todayButton.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
            todayButton.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        return today
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(context: Context, selectedButton: Button, buttons: List<Button>, selectedDate: LocalDate, onSelectDate: (LocalDate) -> Unit) :LocalDate{
        buttons.forEach { button ->
            if (button == selectedButton) {
                button.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
                button.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                button.backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
                button.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
        onSelectDate(selectedDate)
        return selectedDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDayDateMonthText(context: Context, date: LocalDate, textView: TextView) {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d'${getDayOfMonthSuffix(date.dayOfMonth)}' MMMM")
        val formattedDate = date.format(formatter)
        textView.text = formattedDate
    }

    private fun getDayOfMonthSuffix(day: Int): String {
        if (day in 11..13) {
            return "th"
        }
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}
