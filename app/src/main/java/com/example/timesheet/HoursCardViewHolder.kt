package com.example.timesheet

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HoursCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.hours_cardview)
    val textHours: TextView = view.findViewById(R.id.text_hours)
    val textHoursType: TextView = view.findViewById(R.id.text_hours_type)
}
