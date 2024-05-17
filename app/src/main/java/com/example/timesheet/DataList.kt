package com.example.timesheet

import java.util.Date

data class DataList(
    val date:Date,
    val timeSheetDataList : MutableList<DataItem>
)
