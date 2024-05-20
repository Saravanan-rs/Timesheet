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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    // Declare buttons as global variables
    private lateinit var date1Button: Button
    private lateinit var date2Button: Button
    private lateinit var date3Button: Button
    private lateinit var date4Button: Button
    private lateinit var date5Button: Button
    private lateinit var date6Button: Button
    private lateinit var date7Button: Button

    @RequiresApi(Build.VERSION_CODES.O)
    //Get current week sunday date as reference to start date of week
    private var currentWeekStartDate: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var dataItems : MutableList<DataItem>
    private lateinit var adapter : DataAdapter
    private var timeSheetDB= mutableListOf<DataList>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDateButtons()
        setupPreviousNextButtonListeners()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        dataItems= mutableListOf()

        //Add task button onclickListener
        findViewById<ImageButton>(R.id.addTasktoTimeSheetImgBtn).setOnClickListener {
            adapter=DataAdapter(dataItems)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            val intentToFormActivity=Intent(this,FormActivity::class.java).apply {
                putExtra("CurrentDayDateMonth",findViewById<TextView>(R.id.dayDateMonthTitleTextView).text.toString())
            }
            startActivityForResult(intentToFormActivity, 1)
        }
    }

    // Outside onCreate() to handle form submission data
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
    private fun addToTimeSheetDB(date: LocalDate, dataItems: MutableList<DataItem>) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = date.format(dateFormatter)

        // Check if the date is already present in the timeSheetDB
        val existingDataList = timeSheetDB.find { it.date == currentDate }

        if (existingDataList != null) {
            // If the date is already present, append the dataItems list
            existingDataList.activities.addAll(dataItems)
        } else {
            // If the date is not present, create a new instance and add it to the timeSheetDB
            val newDataList = DataList(currentDate, dataItems.toMutableList()) // Create a new list to avoid modifying the original list
            timeSheetDB.add(newDataList)
        }
    }


    //listener for buttons < and > to move btw weeks and update calendar according to that
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPreviousNextButtonListeners() {

        val dateRangeTextView: TextView = findViewById(R.id.date_range_text_view)
        updateDateRangeText(dateRangeTextView, currentWeekStartDate)

        val previousWeekButton: ImageButton = findViewById(R.id.imgBtnWeekBefore)
        val nextWeekButton: ImageButton = findViewById(R.id.imgBtnWeekAfter)

        previousWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.minusWeeks(1)
            updateDateRangeText(dateRangeTextView, currentWeekStartDate)
            updateDayDateMonthText(currentWeekStartDate)
            selectDate(date1Button,currentWeekStartDate)
        }

        nextWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1)
            updateDateRangeText(dateRangeTextView, currentWeekStartDate)
            updateDayDateMonthText(currentWeekStartDate)
            selectDate(date1Button,currentWeekStartDate)
        }
    }

    //Updating week date range title
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateRangeText(textView: TextView, startDate: LocalDate) {
        val buttons = listOf(date1Button, date2Button, date3Button, date4Button, date5Button, date6Button, date7Button)
        val formatterDate = DateTimeFormatter.ofPattern("d")
        buttons.forEachIndexed { index, button ->
            button.text = startDate.plusDays(index.toLong()).format(formatterDate)
        }

        val endDate = startDate.plusDays(6)
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val startOfWeekFormatted = startDate.format(formatter)
        val endOfWeekFormatted = endDate.format(formatter)
        val dateRangeText="$startOfWeekFormatted - $endOfWeekFormatted"
        textView.text = dateRangeText
    }


    //initialise custom calendar buttons and onclick listeners
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDateButtons() {
        // Initialize buttons
        date1Button = findViewById(R.id.date1Button)
        date2Button = findViewById(R.id.date2Button)
        date3Button = findViewById(R.id.date3Button)
        date4Button = findViewById(R.id.date4Button)
        date5Button = findViewById(R.id.date5Button)
        date6Button = findViewById(R.id.date6Button)
        date7Button = findViewById(R.id.date7Button)
        val buttons = listOf(date1Button, date2Button, date3Button, date4Button, date5Button, date6Button, date7Button)
        val formatterDate = DateTimeFormatter.ofPattern("d")
        buttons.forEachIndexed { index, button ->
            button.text = currentWeekStartDate.plusDays(index.toLong()).format(formatterDate)
            button.setOnClickListener { selectDate(button,currentWeekStartDate.plusDays(index.toLong())) }
        }

        // Highlight today's date button
        highlightTodayButton()
    }


    //function to highlight today date button when starting the app
    @RequiresApi(Build.VERSION_CODES.O)
    private fun highlightTodayButton() {
        val buttons = listOf(date1Button, date2Button, date3Button, date4Button, date5Button, date6Button, date7Button)
        val today = LocalDate.now()

        val todayIndex = currentWeekStartDate.until(today, ChronoUnit.DAYS).toInt()
        if (todayIndex >= 0 && todayIndex < buttons.size) {
            val todayButton = buttons[todayIndex]
            todayButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
            todayButton.setTextColor(ContextCompat.getColor(this, R.color.white)) // Change text color to white
        }
        updateDayDateMonthText(today)
    }

    //check for button clicked in custom calendar and highlight that buttons, remove highlight in other buttons
    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectDate(selectedButton: Button,selectedDate: LocalDate) {
        val buttons = listOf(date1Button, date2Button, date3Button, date4Button, date5Button, date6Button, date7Button)
        for (button in buttons) {
            if (button == selectedButton) {
                button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
                button.setTextColor(ContextCompat.getColor(this, R.color.white)) // Change text color to white
            } else {
                button.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.transparent)
                button.setTextColor(ContextCompat.getColor(this, R.color.black)) // Change text color to black
            }
        }
        updateDayDateMonthText(selectedDate)

    }

    //calculate total hours and leave hours and update it to text view
    private fun calculateTotalHours(dataItems: MutableList<DataItem>){
        val totalHours = dataItems.sumOf { it.hours }
        findViewById<TextView>(R.id.text_total_hours).text = totalHours.toString()
        val leaveHours = 9 - totalHours
        findViewById<TextView>(R.id.text_leave_hours).text = if (leaveHours > 0) leaveHours.toString() else "0"
    }

    //Update the contents of list in recyclerview, when ever change of date or week
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(dataItems: MutableList<DataItem>)
    {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        adapter=DataAdapter(dataItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        calculateTotalHours(dataItems)
    }

    // Create a function to update the day, date, and month in the Title of RecyclerView List
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDayDateMonthText(date: LocalDate) {
        dataItems= mutableListOf()
        updateRecyclerView(dataItems)
        val formatter = DateTimeFormatter.ofPattern("EEEE, d'${getDayOfMonthSuffix(date.dayOfMonth)}' MMMM", Locale.getDefault())
        val formattedDate = date.format(formatter)
        findViewById<TextView>(R.id.dayDateMonthTitleTextView).text = formattedDate
    }

    // Function to get the suffix for the day of the month (e.g., "st", "nd", "rd", "th")
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
