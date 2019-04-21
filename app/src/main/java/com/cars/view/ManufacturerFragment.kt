package com.cars.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cars.R
import com.cars.model.Manufacturer
import com.cars.presenter.ManufacturerPresenter

class ManufacturerFragment : BaseFragment<Manufacturer>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = ManufacturerPresenter(resources.getInteger(R.integer.page_size))
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.app_name)
    }
}
