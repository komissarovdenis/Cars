package com.cars.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cars.R
import com.cars.model.DataModel
import com.cars.presenter.BasePresenter

class CarFragment : AnimationFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.car_info)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_car, container, false)
        val manufacturerView = view.findViewById<TextView>(R.id.manufacturer_view)
        val modelView = view.findViewById<TextView>(R.id.model_view)
        val yearView = view.findViewById<TextView>(R.id.year_view)

        arguments?.apply {
            val manufacturer = getParcelable<DataModel>(BasePresenter.MANUFACTURER)
            val model = getParcelable<DataModel>(BasePresenter.MAIN_TYPE)
            val year = getParcelable<DataModel>(BasePresenter.YEAR)

            manufacturerView.text = getString(R.string.car_manufacturer, manufacturer.getDisplayData())
            modelView.text = getString(R.string.car_model, model.getDisplayData())
            yearView.text = getString(R.string.car_year, year.getDisplayData())
        }

        return view
    }
}
