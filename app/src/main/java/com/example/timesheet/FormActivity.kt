package com.example.timesheet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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

        val timeTypeSpinner = findViewById<Spinner>(R.id.timeTypeSpinner)
        val projectCodeSpinner = findViewById<Spinner>(R.id.projectCodeSpinner)
        val activityTypeSpinner = findViewById<Spinner>(R.id.activityTypeSpinner)

        val projectCodeTextView = findViewById<TextView>(R.id.tvProjectCode)
        val activityTypeTextView = findViewById<TextView>(R.id.tvActivityType)

        val timeTypeRequired = listOf(
            "Business Travel",
            "Full Day Leave",
            "Half Day Leave",
            "Holiday",
            "Presales",
            "Project Work",
            "Training (Trainee)",
            "Training (Trainer)",
            "Workshop"
        )
        val timeType = timeTypeSpinner.selectedItem.toString()
        var projectCode = "--"
        var activityType = "--"

        timeTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedTimeType = parent.getItemAtPosition(position).toString()
                if (selectedTimeType in timeTypeRequired) {
                    projectCodeTextView.visibility = View.VISIBLE
                    projectCodeSpinner.visibility = View.VISIBLE
                    activityTypeSpinner.visibility = View.VISIBLE
                    activityTypeTextView.visibility = View.VISIBLE
                    projectCode = projectCodeSpinner.selectedItem.toString()
                    activityType = activityTypeSpinner.selectedItem.toString()
                } else {
                    projectCodeTextView.visibility = View.GONE
                    projectCodeSpinner.visibility = View.GONE
                    activityTypeSpinner.visibility = View.GONE
                    activityTypeTextView.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        findViewById<Button>(R.id.addTaskFormBtn).setOnClickListener {

            val hours = findViewById<EditText>(R.id.hours).text.toString()

            if (hours != "" && hours.toDouble() > 0 && hours.toDouble() <= 24) {
                val resultIntent = Intent()
                resultIntent.putExtra("TimeType", timeType)
                resultIntent.putExtra("ProjectCode", projectCode)
                resultIntent.putExtra("ActivityType", activityType)
                resultIntent.putExtra("Hours", hours)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please Enter valid Hours", Toast.LENGTH_LONG).show()
            }
        }
        findViewById<Button>(R.id.cancelTaskFormBtn).setOnClickListener {
            finish()
        }

    }
}

private fun Spinner.onItemSelectedListener(value: () -> Unit) {

}
