package com.example.timesheet

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


@Suppress("DEPRECATION")
class WeeklyOverviewActivity : AppCompatActivity(), LocationBottomSheetDialog.OnLocationSelectedListener {

    private lateinit var date1Button: Button
    private lateinit var date2Button: Button
    private lateinit var date3Button: Button
    private lateinit var date4Button: Button
    private lateinit var date5Button: Button
    private lateinit var date6Button: Button
    private lateinit var date7Button: Button

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var currentWeekStartDate:LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var selectedDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weekly_timesheet_layout)

        currentWeekStartDate = intent.getSerializableExtra("currentWeekStartDate") as LocalDate? ?: LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        selectedDate=intent.getSerializableExtra("selectedDate")as LocalDate
        initializeDateButtons()
        setupPreviousNextButtonListeners()
        val intentToMainActivity = Intent(this,MainActivity::class.java)
        findViewById<TextView>(R.id.backButton).setOnClickListener {
            startActivity(intentToMainActivity)
        }

        findViewById<ImageButton>(R.id.backImageButton).setOnClickListener{
            startActivity(intentToMainActivity)
        }
        findViewById<ImageButton>(R.id.locationMoreButton).setOnClickListener {
            val bottomSheet = LocationBottomSheetDialog()
            bottomSheet.show(supportFragmentManager, "MyBottomSheetDialogFragment")
        }

        val recyclerView: RecyclerView = findViewById(R.id.hours_card_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Change the number of columns as needed

        val items = listOf(
            CardItem("54", "Regular Hours", Color.parseColor("#E1BEE7"), Color.BLUE),
            CardItem("40", "Overtime Hours", Color.parseColor("#C5E1A5"), Color.parseColor("#195E2A")),
            CardItem("20", "Vacation Hours", Color.parseColor("#FFCDD2"), Color.RED),
        )
        val hoursCardViewAdapter = HoursCardViewAdapter(this, items)
        recyclerView.adapter = hoursCardViewAdapter

    }
    override fun onLocationSelected(location: String) {
        val locationText=findViewById<TextView>(R.id.locationText)
        locationText.text = location
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
             }
        }

        nextWeekButton.setOnClickListener {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1)
            Utils.updateDateRangeText(this, dateRangeTextView, currentWeekStartDate, getButtons())
            selectedDate=Utils.selectDate(this, date1Button, getButtons(), currentWeekStartDate) { date ->
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDateButtons() {
        val buttons = getButtons()
        Utils.initializeDateButtons(this, currentWeekStartDate, buttons) { date ->
            // Optional: implement any additional behavior needed for this activity when a date is selected
        }
        Utils.highlightTodayButton(this,buttons,selectedDate)
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
}
