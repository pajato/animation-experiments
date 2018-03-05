package com.pajato.drawerreplacement

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.util.TypedValue
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_drawer.*

class MainActivity : AppCompatActivity(), SlidingUpPanelLayout.PanelSlideListener {
    private lateinit var recyclerViewManager: RecyclerViewManager
    private var isPlaying = false

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        // Animate the panel's entrance / exit depending on user choice.
        AnimationHelper.animate(slideOffset, this)
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
        // Prevent button clicking while the panel is being dragged to help reduce unnecessary anchoring.
        if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            updateAnimBmps(previousState)
            playButton.isClickable = false
            next.isClickable = false
            previous.isClickable = false
        } else {
            playButton.isClickable = true
            next.isClickable = true
            previous.isClickable = true
        }

        // To reduce possible problems with fast scrolling, when the panel enters an expanded or
        // collapsed state, set all the views to a standard position / size.
        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            AnimationHelper.drawerOpen(this)
            invisTitle.textSize = 16.0f
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            AnimationHelper.drawerClosed(this)
            invisTitle.textSize = 36.0f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Dimens.init(this)
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
        recyclerViewManager = RecyclerViewManager(this, adapter, layoutManager)
        horizontalSlider.addOnScrollListener(recyclerViewManager)
        recyclerViewManager.updateSongInformation(0)
    }

    fun changeSong(view: View) {
        recyclerViewManager.changeSong(view)
    }

    fun swapPlayPause(view: View) {
        if (view.id == playButton.id) {
            playButton.setImageResource(if (isPlaying) R.drawable.ic_play_arrow_black_24dp else R.drawable.ic_pause_black_24dp)
            isPlaying = !isPlaying
        }
    }

    fun getDimen(resId: Int): Float {
        val outValue = TypedValue()
        this.resources.getValue(resId, outValue, true)
        return outValue.float
    }

    private fun updateAnimBmps(previousState: SlidingUpPanelLayout.PanelState) {
        // When the panel is expanded, invisible title will be small and displayed will be large.
        // We rebuild the drawing cache to update BMPs when the text changes.
        if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            invisTitle.destroyDrawingCache()
            invisTitle.buildDrawingCache()
            songTitle.destroyDrawingCache()
            songTitle.buildDrawingCache()
            AnimationHelper.setTitleBmps(songTitle?.drawingCache?.copy(Bitmap.Config.ARGB_8888, true),
                    invisTitle?.drawingCache?.copy(Bitmap.Config.ARGB_8888, true))
            // When the panel is collapsed, invisible title will be large and displayed will be small.
        } else if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            invisTitle.destroyDrawingCache()
            invisTitle.buildDrawingCache()
            songTitle.destroyDrawingCache()
            songTitle.buildDrawingCache()
            AnimationHelper.setTitleBmps(songTitle?.drawingCache?.copy(Bitmap.Config.ARGB_8888, true),
                    invisTitle?.drawingCache?.copy(Bitmap.Config.ARGB_8888, true))
        }
    }
}
