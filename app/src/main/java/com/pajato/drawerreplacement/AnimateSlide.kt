package com.pajato.drawerreplacement

import android.support.constraint.ConstraintLayout
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout

/**
 * Created by support on 2/10/18.
 */
class AnimateSlide : SlidingUpPanelLayout.PanelSlideListener {
    override fun onPanelSlide(panel: View, slideOffset: Float) {
        val step : Int = (slideOffset * 100).toInt()
        animateSlideIn(step, panel as ConstraintLayout)
    }

    override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {

    }

    private fun animateSlideIn(step: Int, panel: ConstraintLayout) {

    }
}