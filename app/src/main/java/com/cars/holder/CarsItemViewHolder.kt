package com.cars.holder

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cars.R
import com.cars.model.DataModel

class CarsItemViewHolder(parent: ViewGroup,
                         clickListener: View.OnClickListener,
                         @ColorRes backgroundColorResId: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.cars_view_item_holder, parent, false)
) {

    private val titleView = itemView.findViewById<TextView>(R.id.title_view)

    init {
        val color = ContextCompat.getColor(parent.context, backgroundColorResId)
        itemView.findViewById<View>(R.id.content_view).setBackgroundColor(color)
        itemView.setOnClickListener(clickListener)
    }

    fun bind(data: DataModel) {
        titleView.text = data.getDisplayData()
        itemView.tag = data
    }
}
