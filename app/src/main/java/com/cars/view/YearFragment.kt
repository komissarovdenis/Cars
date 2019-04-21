package com.cars.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cars.R
import com.cars.model.DataModel
import com.cars.model.Year
import com.cars.presenter.BasePresenter
import com.cars.presenter.YearPresenter

class YearFragment : BaseFragment<Year>() {

    private var manufacturer: DataModel? = null
    private var model: DataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val manufacturer = arguments?.getParcelable<DataModel>(BasePresenter.MANUFACTURER)
        val model = arguments?.getParcelable<DataModel>(BasePresenter.MAIN_TYPE)
        if (manufacturer != null && model != null) {
            presenter = YearPresenter(manufacturer, model, resources.getInteger(R.integer.page_size))
        }

        this.model = model
        this.manufacturer = manufacturer
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val model = this.model
        val manufacturer = this.manufacturer
        if (manufacturer != null && model != null) {
            (activity as? AppCompatActivity)?.supportActionBar?.title =
                manufacturer.getDisplayData() + ", " + model.getDisplayData()
        }
    }
}
