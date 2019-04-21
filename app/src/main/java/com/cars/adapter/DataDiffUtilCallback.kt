package com.cars.adapter

import android.support.v7.util.DiffUtil
import com.cars.model.DataModel

class DataDiffUtilCallback(private val old: List<DataModel>,
                           private val new: List<DataModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].getDataId() == new[newItemPosition].getDataId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].getDisplayData() == new[newItemPosition].getDisplayData()
    }
}