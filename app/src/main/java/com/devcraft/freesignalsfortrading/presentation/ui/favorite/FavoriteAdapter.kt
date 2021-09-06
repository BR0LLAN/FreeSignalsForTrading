package com.devcraft.freesignalsfortrading.presentation.ui.favorite

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import io.paperdb.Paper
import kotlinx.android.synthetic.main.single_item_fav.view.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteVH>() {

    private var selectedPositionItem: Int = RecyclerView.NO_POSITION

    var items: MutableList<QuotesCurrency> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVH {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.single_item_fav, parent, false)
        Paper.init(parent.context)
        return FavoriteVH(v)
    }

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {
        holder.bind(items[position], position)

    }

    override fun getItemCount() = items.size

    inner class FavoriteVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("SetTextI18n")
        fun bind(item: QuotesCurrency, position: Int) {
            itemView.run {

                quote_name.text = item.symbol.replaceRange(3,3,"/")
                quote_value.text = item.close.toString()

                if (selectedPositionItem == position){
                    itemView.setBackgroundColor(Color.parseColor("#550095ff"))
                    Paper.book().write("symbolForDeleteFavorite",item.symbol)
                    Paper.book().write("positionFavItem", position)
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



