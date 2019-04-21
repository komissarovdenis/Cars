package com.cars.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cars.MainActivity
import com.cars.model.DataModel
import com.cars.model.Parser
import com.cars.model.Year
import okhttp3.HttpUrl
import org.json.JSONObject

class YearPresenter(private val manufacturer: DataModel,
                    private val model: DataModel,
                    pageSize: Int) : BasePresenter<Year>(pageSize) {

    override fun createRequestUrl(page: Int): HttpUrl =
        HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegments(PATH)
            .addPathSegment(BUILD_DATES)
            .addQueryParameter(MANUFACTURER, manufacturer.getDataId())
            .addQueryParameter(MAIN_TYPE, model.getDataId())
            .addQueryParameter(KEY, API_KEY)
            .build()

    override fun parseData(json: JSONObject): List<Year> = Parser.parseYear(json.getJSONObject(DATA))

    override fun onItemClicked(context: Context, item: DataModel) {
        val bundle = Bundle()
        bundle.putParcelable(YEAR, item)
        bundle.putParcelable(MAIN_TYPE, model)
        bundle.putParcelable(MANUFACTURER, manufacturer)
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.DATA_BUNDLE, bundle)
        intent.putExtra(MainActivity.FRAGMENT, MainActivity.CAR_FRAGMENT)
        context.startActivity(intent)
    }
}
