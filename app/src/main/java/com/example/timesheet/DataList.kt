package com.example.timesheet

data class DataList(
    val date: String, // Assuming date is represented as a string for simplicity
    val activities: MutableList<DataItem>
)
