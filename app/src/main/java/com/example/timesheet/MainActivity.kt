package com.example.timesheet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var date1TextView: TextView
    private lateinit var date2TextView: TextView
    private lateinit var date3TextView: TextView
    private lateinit var date4TextView: TextView
    private lateinit var date5TextView: TextView
    private lateinit var date6TextView: TextView
    private lateinit var date7TextView: TextView

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

        initializeDateTextViews()
        setupPreviousNextButtonListeners()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        dataItems = mutableListOf()

        findViewById<ExtendedFloatingActionButton>(R.id.timeEntry_button).setOnClickListener {
            val intentToTimeEntry = Intent(this, WeeklyOverviewActivity::class.java).apply {
                putExtra("currentWeekStartDate", currentWeekStartDate)
                putExtra("selectedDate", selectedDate)
            }
            startActivity(intentToTimeEntry)
        }
        findViewById<ImageButton>(R.id.addTasktoTimeSheetImgBtn).setOnClickListener {
            adapter = DataAdapter(dataItems)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            val intentToFormActivity = Intent(this, FormActivity::class.java).apply {
                putExtra("CurrentDayDateMonth", findViewById<TextView>(R.id.dayDateMonthTitleTextView).text.toString())
            }
            startActivityForResult(intentToFormActivity, 1)
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
        Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())

        val previousWeekButton: ImageButton = findViewById(R.id.imgBtnWeekBefore)
        val nextWeekButton: ImageButton = findViewById(R.id.imgBtnWeekAfter)

        previousWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.minusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())
            selectedDate = Utils.selectDate(this, date1TextView, getTextViews(), currentWeekStartDate) { date ->
                updateDayDateMonthText(date)
            }
        }

        nextWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())
            selectedDate = Utils.selectDate(this, date1TextView, getTextViews(), currentWeekStartDate) { date ->
                updateDayDateMonthText(date)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDateTextViews() {
        val textViews = getTextViews()
        Utils.initializeDateTextViews(this, currentWeekStartDate, textViews) { date ->
            updateDayDateMonthText(date)
        }
        val today = Utils.highlightTodayTextView(this, textViews, currentWeekStartDate)
        updateDayDateMonthText(today)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTextViews(): List<TextView> {
        date1TextView = findViewById(R.id.date1Button)
        date2TextView = findViewById(R.id.date2Button)
        date3TextView = findViewById(R.id.date3Button)
        date4TextView = findViewById(R.id.date4Button)
        date5TextView = findViewById(R.id.date5Button)
        date6TextView = findViewById(R.id.date6Button)
        date7TextView = findViewById(R.id.date7Button)
        return listOf(date1TextView, date2TextView, date3TextView, date4TextView, date5TextView, date6TextView, date7TextView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDayDateMonthText(date: LocalDate) {
        val dayDateMonthTextView: TextView = findViewById(R.id.dayDateMonthTitleTextView)
        Utils.updateDayDateMonthText(this, date, dayDateMonthTextView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateTotalHours(dataItems: List<DataItem>) {
        val totalHoursTextView: TextView = findViewById(R.id.text_total_hours)
        val totalHours = dataItems.sumOf { it.hours }
        totalHoursTextView.text = totalHours.toString()
        val leaveHours = 9 - totalHours
        findViewById<TextView>(R.id.text_leave_hours).text = if (leaveHours > 0) leaveHours.toString() else "0"
    }
}
