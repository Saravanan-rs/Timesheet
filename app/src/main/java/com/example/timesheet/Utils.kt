@file:Suppress("NAME_SHADOWING")

package com.example.timesheet

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Utils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDateRangeText(
        context: Context,
        textView: TextView,
        startDate: LocalDate,
        textViews: List<TextView>,
    ) {
        val formatterDate = DateTimeFormatter.ofPattern("d")
        textViews.forEachIndexed { index, textView ->
            textView.text = startDate.plusDays(index.toLong()).format(formatterDate)
        }

        val endDate = startDate.plusDays(6)
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val startOfWeekFormatted = startDate.format(formatter)
        val endOfWeekFormatted = endDate.format(formatter)
        val dateRangeText = "$startOfWeekFormatted - $endOfWeekFormatted"
        textView.text = dateRangeText
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initializeDateTextViews(
        context: Context,
        currentWeekStartDate: LocalDate,
        textViews: List<TextView>,
        onSelectDate: (LocalDate) -> Unit,
    ) {
        val formatterDate = DateTimeFormatter.ofPattern("d")
        textViews.forEachIndexed { index, textView ->
            textView.text = currentWeekStartDate.plusDays(index.toLong()).format(formatterDate)
            textView.setOnClickListener {
                selectDate(
                    context,
                    textView,
                    textViews,
                    currentWeekStartDate.plusDays(index.toLong()),
                    onSelectDate
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun highlightTodayTextView(
        context: Context,
        textViews: List<TextView>,
        currentWeekStartDate: LocalDate,
    ): LocalDate {
        val today = LocalDate.now()
        val todayIndex = currentWeekStartDate.until(today, ChronoUnit.DAYS).toInt()
        if (todayIndex in textViews.indices) {
            val todayTextView = textViews[todayIndex]
            todayTextView.setBackgroundResource(R.drawable.circle_highlight)
            todayTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        return today
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(
        context: Context,
        selectedTextView: TextView,
        textViews: List<TextView>,
        selectedDate: LocalDate,
        onSelectDate: (LocalDate) -> Unit,
    ): LocalDate {
        textViews.forEach { textView ->
            if (textView == selectedTextView) {
                textView.setBackgroundResource(R.drawable.circle_highlight)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                textView.setBackgroundResource(android.R.color.transparent)
                textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
        onSelectDate(selectedDate)
        return selectedDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDayDateMonthText(context: Context, date: LocalDate, textView: TextView) {
        val formatter =
            DateTimeFormatter.ofPattern("EEEE, d'${getDayOfMonthSuffix(date.dayOfMonth)}' MMMM")
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
