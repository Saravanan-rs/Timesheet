package com.example.timesheet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var date1Button: Button
    private lateinit var date2Button: Button
    private lateinit var date3Button: Button
    private lateinit var date4Button: Button
    private lateinit var date5Button: Button
    private lateinit var date6Button: Button
    private lateinit var date7Button: Button

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentWeekStartDate: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDate = LocalDate.now()


    private lateinit var dataItems: MutableList<DataItem>
    private lateinit var adapter: DataAdapter
    private var timeSheetDB = mutableListOf<DataList>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDateButtons()
        setupPreviousNextButtonListeners()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        dataItems = mutableListOf()

        findViewById<ImageButton>(R.id.addTasktoTimeSheetImgBtn).setOnClickListener {
            adapter = DataAdapter(dataItems)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            val intentToFormActivity = Intent(this, FormActivity::class.java).apply {
                putExtra("CurrentDayDateMonth", findViewById<TextView>(R.id.dayDateMonthTitleTextView).text.toString())
            }
            startActivityForResult(intentToFormActivity, 1)
        }
        findViewById<ImageButton>(R.id.more_timesheet_detail).setOnClickListener {
            val intentToMore = Intent(this, WeeklyOverviewActivity::class.java).apply {
                putExtra("currentWeekStartDate", currentWeekStartDate)
                putExtra("selectedDate",selectedDate)
            }
            startActivity(intentToMore)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val timeType = data?.getStringExtra("TimeType")
            val projectCode = data?.getStringExtra("ProjectCode")
            val activityType = data?.getStringExtra("ActivityType")
            val hours = data?.getStringExtra("Hours")?.toDouble()

            val newDataItem = DataItem(timeType ?: "", projectCode ?: "", activityType ?: "", hours ?: 0.0)
            dataItems.add(newDataItem)
            calculateTotalHours(dataItems)
            adapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPreviousNextButtonListeners() {
        val dateRangeTextView: TextView = findViewById(R.id.date_range_text_view)
        Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getButtons())

        val previousWeekButton: ImageButton = findViewById(R.id.imgBtnWeekBefore)
        val nextWeekButton: ImageButton = findViewById(R.id.imgBtnWeekAfter)

        previousWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.minusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getButtons())
            selectedDate=Utils.selectDate(this, date1Button, getButtons(), currentWeekStartDate) { date ->
                updateDayDateMonthText(date)
            }
        }

        nextWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getButtons())
            selectedDate=Utils.selectDate(this, date1Button, getButtons(), currentWeekStartDate) { date ->
                updateDayDateMonthText(date)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDateButtons() {
        val buttons = getButtons()
        Utils.initializeDateButtons(this, currentWeekStartDate, buttons) { date ->
            updateDayDateMonthText(date)
        }
        val today=Utils.highlightTodayButton(this,buttons,currentWeekStartDate)
        updateDayDateMonthText(today)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getButtons(): List<Button> {
        date1Button = findViewById(R.id.date1Button)
        date2Button = findViewById(R.id.date2Button)
        date3Button = findViewById(R.id.date3Button)
        date4Button = findViewById(R.id.date4Button)
        date5Button = findViewById(R.id.date5Button)
        date6Button = findViewById(R.id.date6Button)
        date7Button = findViewById(R.id.date7Button)
        return listOf(date1Button, date2Button, date3Button, date4Button, date5Button, date6Button, date7Button)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDayDateMonthText(date: LocalDate) {
        val dayDateMonthTitleTextView: TextView = findViewById(R.id.dayDateMonthTitleTextView)
        Utils.updateDayDateMonthText(this, date, dayDateMonthTitleTextView)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateTotalHours(dataItems: List<DataItem>) {
        val totalHoursTextView: TextView = findViewById(R.id.text_total_hours)
        val totalHours = dataItems.sumOf { it.hours }
        totalHoursTextView.text = totalHours.toString()
        val leaveHours = 9 - totalHours
        findViewById<TextView>(R.id.text_leave_hours).text = if (leaveHours > 0) leaveHours.toString() else "0"
    }
}
