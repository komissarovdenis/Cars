package com.cars.presenter

import android.util.Log
import com.cars.Contract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

abstract class BasePresenter<T>(protected var pageSize: Int) : Contract.Presenter<T> {

    private val httpClient = OkHttpClient()
    private var disposable: Disposable? = null
    private var view: Contract.View<T>? = null
    private val dataSet = ArrayList<T>()
    private var totalPageCount = 0
    private var loading = false
    private var page = 0

    override fun onViewCreated(view: Contract.View<T>) {
        this.view = view
        view.updateGlobalLoader(loading)
        view.onNewDataSet()
    }

    override fun onDestroyView() {
        view = null
    }

    override fun onCreate() {
        reload()
    }

    override fun onDestroy() {
        dataSet.clear()
        disposable?.dispose()
    }

    override fun getDataSet(): List<T> = dataSet

    override fun canLoadMore(): Boolean = page < totalPageCount || totalPageCount == 0

    override fun isLoading(): Boolean = loading

    override fun reload() {
        page = 0
        totalPageCount = 0
        load(createRequestUrl(0), reload = true)
    }

    override fun loadNext() {
        load(createRequestUrl(page + 1), reload = false)
    }

    private fun load(url: HttpUrl, reload: Boolean) {
        loading = true
        updateLoader(reload)
        disposable = Observable.defer<Response> {
            try {
                val request = Request.Builder().url(url).get().build()
                Observable.just(httpClient.newCall(request).execute())
            } catch (e: Throwable) {
                Observable.error(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .doOnNext { response ->
                if (reload) {
                    dataSet.clear()
                }
                val data = response.body()?.string()
                val json = JSONObject(data)
                totalPageCount = json.optInt(TOTAL_PAGE_COUNT, 1)
                pageSize = json.optInt(PAGE_SIZE, pageSize)
                page = json.optInt(PAGE, 1)
                dataSet.addAll(parseData(json))
                view?.onNewDataSet()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loading = false
                    updateLoader(reload)
                }, { e ->
                    Log.e(TAG, e.message)
                    view?.onError(e)
                    loading = false
                    updateLoader(reload)
                }
            )
    }

    private fun updateLoader(reload: Boolean) {
        view?.apply {
            if (reload) updateGlobalLoader(isLoading()) else updatePageLoader(isLoading())
        }
    }

    abstract fun parseData(json: JSONObject): List<T>

    abstract fun createRequestUrl(page: Int): HttpUrl

    companion object {
        const val TAG = "BasePresenter"
        const val SCHEME = "http"
        const val PAGE = "page"
        const val PAGE_SIZE = "pageSize"
        const val KEY = "wa_key"
        const val TOTAL_PAGE_COUNT = "totalPageCount"

        // ---- Removed. Place actual credentials here ----
        const val HOST = ""
        const val PATH = ""
        const val DATA = ""
        const val API_KEY = ""
        // ------------------------------------------------

        const val MANUFACTURER = "manufacturer"
        const val MAIN_TYPES = "main-types"
        const val MAIN_TYPE = "main-type"
        const val BUILD_DATES = "built-dates"
        const val YEAR = "year"
    }
}
