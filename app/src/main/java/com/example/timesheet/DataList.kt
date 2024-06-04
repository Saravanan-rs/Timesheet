package com.example.timesheet
//Full Database for TIMESHEET
data class DataList(
    val date: String, // Assuming date is represented as a string for simplicity
    val activities: MutableList<DataItem>
)
