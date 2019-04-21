package com.cars.view

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cars.Contract
import com.cars.R
import com.cars.adapter.Adapter
import com.cars.adapter.DataDiffUtilCallback
import com.cars.model.DataModel
import com.cars.presenter.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

abstract class BaseFragment<T: DataModel> : AnimationFragment(),
    Contract.View<T>,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recycler: RecyclerView

    private val pageLoadingRunnable = Runnable {
        presenter?.let { adapter.setLoadingMode(it.isLoading()) }
    }

    private val itemOnClickListener = View.OnClickListener { view ->
        (view?.tag as? DataModel)?.let {
            presenter?.onItemClicked(view.context, it)
        }
    }

    private val handler = Handler()
    private val adapter = Adapter<T>(itemOnClickListener)
    private var lastCheckTime = 0L
    protected var presenter: BasePresenter<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter?.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base, container, false)

        swipeToRefresh = view.findViewById(R.id.swipe_to_refresh_view)
        swipeToRefresh.setOnRefreshListener(this)

        layoutManager = LinearLayoutManager(inflater.context)
        recycler = view.findViewById(R.id.recycler_view)
        recycler.addOnScrollListener(ScrollListener())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.onViewCreated(this@BaseFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.onDestroyView()
        handler.removeCallbacks(pageLoadingRunnable)
    }

    override fun onRefresh() {
        presenter?.reload()
    }

    override fun updateGlobalLoader(loading: Boolean) {
        swipeToRefresh.isRefreshing = loading
    }

    override fun updatePageLoader(loading: Boolean) {
        handler.removeCallbacks(pageLoadingRunnable)
        if (loading) {
            recycler.post { adapter.setLoadingMode(loading) }
        } else {
            handler.postDelayed(pageLoadingRunnable, LOADING_HIDE_DELAY)
        }
    }

    override fun onNewDataSet() {
        presenter?.let {
            val newDataSet = it.getDataSet()
            Observable.just(it)
                .subscribeOn(Schedulers.computation())
                .map {
                    DiffUtil.calculateDiff(DataDiffUtilCallback(adapter.dataSet, newDataSet))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ diffResult ->
                    adapter.dataSet.clear()
                    adapter.dataSet.addAll(newDataSet)
                    diffResult.dispatchUpdatesTo(adapter)
                    lastCheckTime = 0L
                }, { e ->
                    Log.e("Data bind", e.message)
                })
        }
    }

    override fun onError(throwable: Throwable) {
        val connectionError = if (throwable is UnknownHostException) {
            R.string.connection_error
        } else {
            R.string.unknown_error
        }
        Toast.makeText(context, connectionError, Toast.LENGTH_SHORT).show()
    }

    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0 && presenter?.isLoading() == false && presenter?.canLoadMore() == true) {
                layoutManager.let {
                    val last = it.findLastVisibleItemPosition()
                    val total = recycler.adapter.itemCount
                    val time = SystemClock.elapsedRealtime()
                    if (total - last <= ITEMS_LEFT_COUNT && time - lastCheckTime >= DEBOUNCE_TIME) {
                        lastCheckTime = time
                        presenter?.loadNext()
                    }
                }
            }
        }
    }

    companion object {
        const val ITEMS_LEFT_COUNT = 5
        const val DEBOUNCE_TIME = 2000
        const val LOADING_HIDE_DELAY = 100L
    }
}
