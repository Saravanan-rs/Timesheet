package com.example.timesheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DataAdapter(private val dataItems: List<DataItem>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTypeTextView: TextView = itemView.findViewById(R.id.text_time_type)
        private val projectCodeTextView: TextView = itemView.findViewById(R.id.text_project_code)
        private val activityTypeTextView: TextView = itemView.findViewById(R.id.text_activity_type)
        private val hoursTextView: TextView = itemView.findViewById(R.id.text_hours)

        fun bind(dataItem: DataItem) {
            timeTypeTextView.text = dataItem.timeType
            projectCodeTextView.text = dataItem.projectCode
            activityTypeTextView.text = dataItem.activityType
            hoursTextView.text = dataItem.hours.toString()
        }
    }
}
