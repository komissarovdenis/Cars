package com.cars

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.cars.view.CarFragment
import com.cars.view.ManufacturerFragment
import com.cars.view.ModelFragment
import com.cars.view.YearFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_view, ManufacturerFragment())
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.apply {
            val fragment = when (getIntExtra(FRAGMENT, 0)) {
                MODEL_FRAGMENT -> ModelFragment()
                YEAR_FRAGMENT -> YearFragment()
                CAR_FRAGMENT -> CarFragment()
                else -> return
            }

            getBundleExtra(DATA_BUNDLE)?.let { data ->
                fragment.arguments = data
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.animator.right_in, R.animator.right_out,
                        R.animator.left_in, R.animator.left_out
                    )
                    .replace(R.id.content_view, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    companion object {
        const val DATA_BUNDLE = "data"
        const val FRAGMENT = "fragment"
        const val MODEL_FRAGMENT = 100
        const val YEAR_FRAGMENT = 101
        const val CAR_FRAGMENT = 102
    }
}
