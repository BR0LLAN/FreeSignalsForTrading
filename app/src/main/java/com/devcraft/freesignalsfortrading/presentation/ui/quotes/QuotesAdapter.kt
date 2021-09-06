package com.devcraft.freesignalsfortrading.presentation.ui.quotes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import io.paperdb.Paper
import kotlinx.android.synthetic.main.single_item_quotes.view.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.*

class QuotesAdapter: RecyclerView.Adapter<QuotesAdapter.QuotesVH>() {

    private var selectedPositionItem: Int = RecyclerView.NO_POSITION

    var items: MutableList<QuotesCurrency> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesVH {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.single_item_quotes, parent, false)
        Paper.init(parent.context)
        return QuotesVH(v)
    }

    override fun onBindViewHolder(holder: QuotesVH, position: Int) {
        holder.bind(items[position], position)

    }

    override fun getItemCount() = items.size

    inner class QuotesVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(item: QuotesCurrency, position: Int) {
            itemView.run {

                val formatter = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = item.datetime

                quote_name.text = item.symbol.replaceRange(3,3,"/")
                time.text = formatter.format(calendar.time).toString().replaceRange(0,11, "")
                quote_value.text = item.close.toString()
                percent.text = String.format("%.5f", item.percent_change) + "%"

                if (selectedPositionItem == position){
                    itemView.setBackgroundColor(Color.parseColor("#550095ff"))
                    Paper.book().write("symbol",item.symbol)
                    Paper.book().write("quoteValue",item.close)
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



