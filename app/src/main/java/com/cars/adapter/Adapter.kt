package com.cars.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cars.R
import com.cars.holder.CarsItemViewHolder
import com.cars.holder.ProgressItemViewHolder
import com.cars.model.DataModel

class Adapter<T: DataModel>(private val clickListener: View.OnClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataSet = ArrayList<T>()
    private var loadingMode = false

    fun setLoadingMode(loading: Boolean) {
        if (loadingMode != loading) {
            loadingMode = loading
            notifyItemChanged(itemCount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (loadingMode && position == itemCount - 1) VIEW_TYPE_PROGRESS
        else if (position % 2 == 0) VIEW_TYPE_EVEN else VIEW_TYPE_ODD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ODD -> CarsItemViewHolder(parent, clickListener, R.color.cars_view_item_background_transparent)
            VIEW_TYPE_EVEN -> CarsItemViewHolder(parent, clickListener, R.color.cars_view_item_background_colored)
            else -> ProgressItemViewHolder(parent)
        }
    }

    override fun getItemCount(): Int = dataSet.size + (if (loadingMode) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ODD,
            VIEW_TYPE_EVEN -> (holder as CarsItemViewHolder).bind(dataSet[position])
            else -> {}
        }
    }

    companion object {
        const val VIEW_TYPE_PROGRESS = 0
        const val VIEW_TYPE_ODD = 1
        const val VIEW_TYPE_EVEN = 2
    }
}
