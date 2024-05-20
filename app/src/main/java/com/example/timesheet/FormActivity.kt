package com.example.timesheet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_form)
        findViewById<TextView>(R.id.dayDateMonthTitleTextView).text =
            intent.getStringExtra("CurrentDayDateMonth")
        findViewById<Button>(R.id.addTaskFormBtn).setOnClickListener {
            val timeType = findViewById<Spinner>(R.id.timeTypeSpinner).selectedItem.toString()
            val projectCode = findViewById<Spinner>(R.id.projectCodeSpinner).selectedItem.toString()
            val activityType = findViewById<Spinner>(R.id.activityTypeSpinner).selectedItem.toString()
            val hours = findViewById<EditText>(R.id.hours).text.toString()
            if(hours!="" && hours.toDouble() >0 && hours.toDouble()<=24){
                val resultIntent = Intent()
                resultIntent.putExtra("TimeType", timeType)
                resultIntent.putExtra("ProjectCode", projectCode)
                resultIntent.putExtra("ActivityType", activityType)
                resultIntent.putExtra("Hours", hours)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            else {
                Toast.makeText(this,"Please Enter valid Hours",Toast.LENGTH_LONG).show()
            }
        }
        findViewById<Button>(R.id.cancelTaskFormBtn).setOnClickListener {
            finish()
        }

    }
}