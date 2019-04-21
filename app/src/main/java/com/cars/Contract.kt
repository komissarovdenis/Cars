package com.cars

import android.content.Context
import com.cars.model.DataModel

interface Contract {
    interface Presenter<T> {
        fun reload()
        fun loadNext()
        fun canLoadMore(): Boolean
        fun isLoading(): Boolean
        fun getDataSet(): List<T>

        fun onCreate()
        fun onDestroy()
        fun onViewCreated(view: View<T>)
        fun onDestroyView()

        fun onItemClicked(context: Context, item: DataModel)
    }

    interface View<T> {
        fun updateGlobalLoader(loading: Boolean)
        fun updatePageLoader(loading: Boolean)
        fun onError(throwable: Throwable)
        fun onNewDataSet()
    }
}
