package com.cars.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cars.MainActivity
import com.cars.model.DataModel
import com.cars.model.Model
import com.cars.model.Parser
import okhttp3.HttpUrl
import org.json.JSONObject

class ModelPresenter(private val manufacturer: DataModel, pageSize: Int) : BasePresenter<Model>(pageSize) {

    override fun createRequestUrl(page: Int): HttpUrl =
        HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegments(PATH)
            .addPathSegment(MAIN_TYPES)
            .addQueryParameter(MANUFACTURER, manufacturer.getDataId())
            .addQueryParameter(PAGE, page.toString())
            .addQueryParameter(PAGE_SIZE, pageSize.toString())
            .addQueryParameter(KEY, API_KEY)
            .build()

    override fun parseData(json: JSONObject): List<Model> = Parser.parseModel(json.getJSONObject(DATA))

    override fun onItemClicked(context: Context, item: DataModel) {
        val bundle = Bundle()
        bundle.putParcelable(MAIN_TYPE, item)
        bundle.putParcelable(MANUFACTURER, manufacturer)
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.DATA_BUNDLE, bundle)
        intent.putExtra(MainActivity.FRAGMENT, MainActivity.YEAR_FRAGMENT)
        context.startActivity(intent)
    }
}
