package com.example.timesheet

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


data class CardItem(val hours: String, val hoursType: String, val backgroundColor: Int, val textColor: Int)

class HoursCardViewAdapter(
    private val context: Context,
    private val items: List<CardItem>
) : RecyclerView.Adapter<HoursCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursCardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hours_card_item, parent, false)
        return HoursCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoursCardViewHolder, position: Int) {
        val item = items[position]
        holder.textHours.text = item.hours
        holder.textHoursType.text = item.hoursType
        holder.textHours.setTextColor(item.textColor)
        holder.cardView.setCardBackgroundColor(item.backgroundColor)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
