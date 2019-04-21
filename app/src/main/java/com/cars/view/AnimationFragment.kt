package com.cars.view

import android.support.v4.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.cars.R

open class AnimationFragment : Fragment() {

    private var destroyed = false

    override fun onDestroyView() {
        super.onDestroyView()
        destroyed = true
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val shouldNotAnimate = enter && destroyed
        destroyed = false
        return if (shouldNotAnimate) {
            AnimationUtils.loadAnimation(activity, R.anim.none)
        } else {
            super.onCreateAnimation(transit, enter, nextAnim)
        }
    }
}