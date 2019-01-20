package io.vendator.dav.calculate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.vendator.dav.calculate.R
import io.vendator.dav.calculate.room.GameStat
import kotlinx.android.synthetic.main.stats_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatsListAdapter(var array: List<GameStat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            when (viewType) {
                0 -> R.layout.stats_first_element
                else -> R.layout.stats_list_item
            },
            parent,
            false
        )
        return GameStatViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) return 0 else 1
    }

    override fun getItemCount() = if (array.isEmpty()) 0 else array.size+1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as GameStatViewHolder
        if (holder.adapterPosition != 0 || position != 0){
            holder.view.tvSerial.text = position.toString()
            holder.view.tvTotal.text = array[position-1].totalProblem.toString().trim()
            holder.view.tvDate.text = simpleDateFormat.format(array[position-1].date).trim()
            holder.view.tvAccuracy.text = String.format("%.2f", array[position-1].accuracy).trim()
            holder.view.tvTime.text = array[position-1].averageTime.toString().trim()
        }
    }


    data class GameStatViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}