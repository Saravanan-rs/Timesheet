package com.example.timesheet
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class TimeEntryListAdapter(
    private val context: Context,
    private val items: List<TimeEntryListItem>
) : RecyclerView.Adapter<TimeEntryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.time_entry_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.timeType
        holder.subtitle.text = item.projectCode+" | "+item.activityType
        holder.count.text = item.hours.toString()
        holder.imageButton.setImageResource(R.drawable.baseline_navigate_next_24)

        holder.imageButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val subtitle: TextView = itemView.findViewById(R.id.textViewSubtitle)
        val count: TextView = itemView.findViewById(R.id.textViewCount)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
    }
}
