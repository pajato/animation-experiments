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
    private lateinit var recyclerViewManager: RecyclerViewManager
    private var isPlaying = false

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        val cs = ConstraintSet()
        cs.clone(mainPanel)
        // Slide the play button, song duration, and song title into the right place
        cs.setVerticalBias(R.id.playButtonMain, (0.015f + slideOffset * 0.735f))
        cs.setHorizontalBias(R.id.playButtonMain, (0.98f - slideOffset * 0.48f))
        cs.setVerticalBias(R.id.songTitleMain, (0.017f + slideOffset * 0.313f))
//        val textHorizBias = (0.05f + slideOffset * 0.45f)
//        cs.setHorizontalBias(R.id.songTitleMain, textHorizBias)
//        cs.setHorizontalBias(R.id.songDurationMain, textHorizBias)
        cs.applyTo(mainPanel)

        // Fade in the previous button and slowly move it further from the play button.
        previousMain.alpha = slideOffset
        val margin = dpToPx(50 * slideOffset)
        val marginEnd = previousMain.layoutParams as ConstraintLayout.LayoutParams
        marginEnd.rightMargin = margin
        mainPanel.updateViewLayout(previousMain, marginEnd)

        // Fade in the next button and slowly move it further from the play button.
        nextMain.alpha = slideOffset
        val marginStart = nextMain.layoutParams as ConstraintLayout.LayoutParams
        marginStart.leftMargin = margin
        mainPanel.updateViewLayout(nextMain, marginStart)

        soundsTextMain.alpha = slideOffset

        // Increase the size of the play button
        val params = playButtonMain.layoutParams
        val side = dpToPx(48 + slideOffset * 24.0f)
        params.width = side
        params.height = side

        // Increase the text sizes, fade in the "Sounds" textview and rotate the chevron.
        // Ensure that the text size changes occurs more towards the center of the slide to allow for smoother transitions.
        // The commented code is to illustrate what occurs between 0.2 and 0.8 in the slide offset.
        // val durationSp = 14.0f + (slideOffset * 4.0f)
        var durationSp = 14.0f + ((slideOffset - 0.2f) * 6.0f)
        durationSp = when {
            durationSp < 14.0f -> 14.0f
            durationSp > 18.0f -> 18.0f
            else -> durationSp
        }
        songDurationMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, durationSp)
        // val durationTitle = 16.0f + (slideOffset * 20.0f)
        var durationTitle = 16.0f + ((slideOffset - 0.2f) * 30.0f)
        durationTitle = when {
            durationTitle < 16.0f -> 16.0f
            durationTitle > 36.0f -> 36.0f
            else -> durationTitle
        }
        songTitleMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, durationTitle)

        chevronClick.rotation = 270 + (180 * slideOffset)
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
        if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            playButtonMain.isClickable = false
            nextMain.isClickable = false
            previousMain.isClickable = false
        } else {
            playButtonMain.isClickable = true
            nextMain.isClickable = true
            previousMain.isClickable = true
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

        // Install a helper to facilitate scrolling like a ViewPager.
        val helper = PagerSnapHelper()
        horizontalSlider.onFlingListener = null
        helper.attachToRecyclerView(horizontalSlider)
        recyclerViewManager = RecyclerViewManager(this.mainPanel, adapter, layoutManager)
        horizontalSlider.addOnScrollListener(recyclerViewManager)
        recyclerViewManager.updateSongInformation(0)

        // Dynamically set the TextView widths.
        val width = resources.displayMetrics.widthPixels
        val usedWidth = ((chevronClick.layoutParams.width + ((chevronClick.layoutParams as ConstraintLayout.LayoutParams).marginStart)) * 2)
        val availableWidth = width - usedWidth
        songTitleMain.layoutParams.width = availableWidth
        songDurationMain.layoutParams.width = availableWidth
    }

    fun changeSong(view: View) {
        recyclerViewManager.changeSong(view)
    }

    fun swapPlayPause(view: View) {
        if (view.id == playButtonMain.id) {
            playButtonMain.setImageResource(if (isPlaying) R.drawable.ic_play_arrow_black_24dp else R.drawable.ic_pause_black_24dp)
            isPlaying = !isPlaying
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }
}
