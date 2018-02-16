package com.pajato.drawerreplacement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.util.TypedValue
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_slide.*
import kotlinx.android.synthetic.main.activity_drawer.*

class MainActivity : AppCompatActivity(), SlidingUpPanelLayout.PanelSlideListener {
    lateinit var recyclerViewManager: RecyclerViewManager

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        val cs = ConstraintSet()
        cs.clone(mainPanel)
        cs.setVerticalBias(R.id.playButtonMain, (0.015f + slideOffset * 0.735f))
        cs.setHorizontalBias(R.id.playButtonMain, (0.98f - slideOffset * 0.48f))
        cs.setHorizontalBias(R.id.songDurationMain, slideOffset * 0.5f)
        cs.setVerticalBias(R.id.songTitleMain, (0.017f + slideOffset * 0.313f))
        cs.setHorizontalBias(R.id.songTitleMain, (0.05f + slideOffset * 0.45f))
        cs.applyTo(mainPanel)

        previousMain.alpha = slideOffset
        val marginEnd = previousMain.layoutParams as ConstraintLayout.LayoutParams
        marginEnd.rightMargin = dpToPx(50 * slideOffset)
        previousMain.layoutParams = marginEnd

        nextMain.alpha = slideOffset
        val marginStart = nextMain.layoutParams as ConstraintLayout.LayoutParams
        marginStart.leftMargin = dpToPx(50 * slideOffset)
        nextMain.layoutParams = marginStart

        soundsTextMain.alpha = slideOffset

        val params = playButtonMain.layoutParams
        params.width = dpToPx(48 + slideOffset * 24.0f)
        params.height = dpToPx(48 + slideOffset * 24.0f)

        songDurationMain.textSize = (14.0f + (slideOffset * 2.0f))
        songTitleMain.textSize = (16.0f + (slideOffset * 16.0f))

        chevronClick.rotation = 270 - (180 * slideOffset)
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {

        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
    }

    override fun onStart() {
        super.onStart()
        slidingPanel.addPanelSlideListener(this)

        // Setup RecyclerView.
        horizontalSlider.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        horizontalSlider.layoutManager = layoutManager

        val adapter = Adapter()
        horizontalSlider.adapter = adapter

        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(horizontalSlider)
        recyclerViewManager = RecyclerViewManager(this.mainPanel, adapter, layoutManager)
        horizontalSlider.addOnScrollListener(recyclerViewManager)
        recyclerViewManager.updateSongInformation(0)
    }

    fun changeSong(view: View) {
        recyclerViewManager.changeSong(view)
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }
}
