package com.devcraft.freesignalsfortrading.presentation.ui.alert

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import io.paperdb.Paper
import kotlinx.android.synthetic.main.single_item_alerts.view.*

class AlertAdapter(private val alertForView: MutableList<QuotesCurrency>) : RecyclerView.Adapter<AlertAdapter.AlertVH>() {

    private var selectedPositionItem: Int = RecyclerView.NO_POSITION
    private lateinit var preferences: SharedPreferences
    private lateinit var dataAlert: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertVH {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.single_item_alerts, parent, false)
        Paper.init(parent.context)
        preferences = parent.context.getSharedPreferences("ForNotify", Context.MODE_PRIVATE)
        return AlertVH(v)
    }

    override fun onBindViewHolder(holder: AlertVH, position: Int) {
        holder.bind(alertForView[position], position)
    }

    override fun getItemCount() = alertForView.size


    inner class AlertVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("SetTextI18n")
        fun bind(item: QuotesCurrency, position: Int) {
            itemView.run {
                val arrData = preferences
                    .getString(item.symbol, "")

                if (!arrData?.isEmpty()!!) {
                    dataAlert =
                        arrData.split(";") as ArrayList<String>
                }

                if (preferences.contains(item.symbol))
                quote_name.text = item.symbol.replaceRange(3,3,"/")
                time.text = dataAlert[3]
                quote_value.text = dataAlert[0]

                if (selectedPositionItem == position){
                    itemView.setBackgroundColor(Color.parseColor("#550095ff"))
                    Paper.book().write("symbolForDelete",item.symbol)
                    Paper.book().write("position", position)
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                    frame_line.setBackgroundResource(R.drawable.line_bottom)
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            notifyItemChanged(selectedPositionItem)
            selectedPositionItem = adapterPosition
            notifyItemChanged(selectedPositionItem)
        }
    }
}

