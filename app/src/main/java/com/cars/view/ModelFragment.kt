package com.cars.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cars.R
import com.cars.model.DataModel
import com.cars.model.Model
import com.cars.presenter.BasePresenter
import com.cars.presenter.ModelPresenter

class ModelFragment : BaseFragment<Model>() {

    private var manufacturer: DataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        manufacturer = arguments?.getParcelable(BasePresenter.MANUFACTURER)
        manufacturer?.let { manufacturer ->
            presenter = ModelPresenter(manufacturer, resources.getInteger(R.integer.page_size))
        }
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        manufacturer?.apply {
            (activity as? AppCompatActivity)?.supportActionBar?.title = getDisplayData()
        }
    }
}
