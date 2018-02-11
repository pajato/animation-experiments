package com.pajato.drawerreplacement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.TypedValue
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_slide.*

class MainActivity : AppCompatActivity(), SlidingUpPanelLayout.PanelSlideListener {
    override fun onPanelSlide(panel: View, slideOffset: Float) {
        val cs = ConstraintSet()
        cs.clone(mainPanel)
        cs.setVerticalBias(R.id.playButtonMain, slideOffset * 0.7f)
        cs.setHorizontalBias(R.id.playButtonMain, (1.0f - slideOffset * 0.5f))
        cs.applyTo(mainPanel)

        previousMain.alpha = slideOffset
        val marginEnd = previousMain.layoutParams as ConstraintLayout.LayoutParams
        marginEnd.rightMargin = dpToPx(50 * slideOffset)
        previousMain.layoutParams = marginEnd

        nextMain.alpha = slideOffset
        val marginStart = nextMain.layoutParams as ConstraintLayout.LayoutParams
        marginStart.leftMargin = dpToPx(50 * slideOffset)
        nextMain.layoutParams = marginStart

        chevronClick.rotation = 270 - (180 * slideOffset)
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
    }

    override fun onResume() {
        super.onResume()
        slidingPanel.addPanelSlideListener(this)
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }
}
