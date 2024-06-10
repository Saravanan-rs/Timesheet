package com.example.timesheet

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Suppress("DEPRECATION")
class WeeklyOverviewActivity : AppCompatActivity(), LocationBottomSheetDialog.OnLocationSelectedListener {

    private lateinit var date1TextView: TextView
    private lateinit var date2TextView: TextView
    private lateinit var date3TextView: TextView
    private lateinit var date4TextView: TextView
    private lateinit var date5TextView: TextView
    private lateinit var date6TextView: TextView
    private lateinit var date7TextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var currentWeekStartDate: LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var selectedDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet)

        currentWeekStartDate = intent.getSerializableExtra("currentWeekStartDate") as LocalDate? ?: LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        selectedDate = intent.getSerializableExtra("selectedDate") as LocalDate
        initializeDateTextViews()
        setupPreviousNextButtonListeners()
        val intentToMainActivity = Intent(this, MainActivity::class.java)

        // Going back to MainActivity
        findViewById<TextView>(R.id.backButton).setOnClickListener {
            startActivity(intentToMainActivity)
        }

        // Going back to MainActivity
        findViewById<ImageButton>(R.id.backImageButton).setOnClickListener {
            startActivity(intentToMainActivity)
        }

        // Location bottom sheet > button
        findViewById<ImageButton>(R.id.locationMoreButton).setOnClickListener {
            val bottomSheet = LocationBottomSheetDialog()
            bottomSheet.show(supportFragmentManager, "MyBottomSheetDialogFragment")
        }

        // Hours Card Grid Recycler View
        val hoursCardRecyclerView: RecyclerView = findViewById(R.id.hours_card_summary_recyclerview)
        hoursCardRecyclerView.layoutManager = GridLayoutManager(this, 3) // Change the number of columns as needed
        val hoursCardItems = listOf(
            CardItem("54", "Regular Hours", Color.parseColor("#E1BEE7"), Color.BLUE),
            CardItem("20", "Total Hours", Color.parseColor("#C5E1A5"), Color.parseColor("#195E2A")),
            CardItem("10", "Leave Hours", Color.parseColor("#FFCDD2"), Color.RED)
        )
        val hoursCardViewAdapter = HoursCardViewAdapter(this, hoursCardItems)
        hoursCardRecyclerView.adapter = hoursCardViewAdapter

        // Time Entry Recycler View
        val timeEntryRecyclerView: RecyclerView = findViewById(R.id.time_entry_recyclerview)
        timeEntryRecyclerView.layoutManager = LinearLayoutManager(this)
        val items = listOf(
            TimeEntryListItem("Project Work", "INC0000123|Design", 5, R.drawable.baseline_navigate_next_24),
            TimeEntryListItem("Project Planning", "INC0000124|Planning", 2, R.drawable.baseline_navigate_next_24),
            TimeEntryListItem("Project Planning", "INC0000124|Planning", 2, R.drawable.baseline_navigate_next_24),
            // Add more items as needed
        )
        val adapter = TimeEntryListAdapter(this, items)
        timeEntryRecyclerView.adapter = adapter
    }

    // Fetch location from bottom sheet and set to TextView
    override fun onLocationSelected(location: String) {
        val locationText = findViewById<TextView>(R.id.locationText)
        locationText.text = location
    }

    // Week Range Selector
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPreviousNextButtonListeners() {
        val dateRangeTextView: TextView = findViewById(R.id.date_range_text_view)
        Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())

        val previousWeekButton: ImageButton = findViewById(R.id.imgBtnWeekBefore)
        val nextWeekButton: ImageButton = findViewById(R.id.imgBtnWeekAfter)

        previousWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.minusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())
            selectedDate = Utils.selectDate(this, date1TextView, getTextViews(), currentWeekStartDate) { }
        }

        nextWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getTextViews())
            selectedDate = Utils.selectDate(this, date1TextView, getTextViews(), currentWeekStartDate) { }
        }
    }

    // Initialize Calendar Date TextViews
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDateTextViews() {
        val textViews = getTextViews()
        Utils.initializeDateTextViews(this, currentWeekStartDate, textViews) {
            // Optional: implement any additional behavior needed for this activity when a date is selected
        }
        Utils.highlightTodayTextView(this, textViews, selectedDate)
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
}
