package com.pajato.drawerreplacement

import android.content.Context
import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.pajato.drawerreplacement.Dimens.ALPHA_END
import com.pajato.drawerreplacement.Dimens.ALPHA_START
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_END
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_SCALE
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_START
import com.pajato.drawerreplacement.Dimens.DURATION_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.DURATION_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.PLAY_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.PLAY_SIDE_DP_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_SIDE_DP_START
import com.pajato.drawerreplacement.Dimens.PLAY_VERTICAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_VERTICAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.SLIDE_OFFSET_COLLAPSED
import com.pajato.drawerreplacement.Dimens.SLIDE_OFFSET_EXPANDED
import com.pajato.drawerreplacement.Dimens.TITLE_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.TITLE_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_END
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_START
import com.pajato.drawerreplacement.Dimens.TITLE_VERTICAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.TITLE_VERTICAL_BIAS_START
import kotlinx.android.synthetic.main.content_drawer.*

object AnimationHelper {
    private lateinit var titleBmpStart: Bitmap
    private lateinit var titleBmpEnd: Bitmap
    private var smallHeight = 0
    private var smallWidth = 0
    private var largeHeight = 0
    private var largeWidth = 0
    private var isOpening = false
    private var durationConnectedToText = true

    /**
     * Update the Title Bitmaps. The startBitmap should be the current TextView's drawable, and the
     * endBitmap should come from a TextView that emulates its desired result.
     */
    fun setTitleBmps(startBitmap: Bitmap?, endBitmap: Bitmap?) {
        if (startBitmap != null && endBitmap != null) {
            this.titleBmpStart = startBitmap
            this.titleBmpEnd = endBitmap

            isOpening = titleBmpStart.width < titleBmpEnd.width

            // Enforce consistent Bitmap sizing. See scaleTextViewWithBitmap. You could use startHeight,
            // endWidth, etc and differentiate later, but it was harder to track and more calculations
            // in frequently-called animation methods, as opposed to here which is called less frequently.
            if (isOpening) {
                this.smallHeight = titleBmpStart.height
                this.smallWidth = titleBmpStart.width
                this.largeHeight = titleBmpEnd.height
                this.largeWidth = titleBmpEnd.width
            } else {
                this.smallHeight = titleBmpEnd.height
                this.smallWidth = titleBmpEnd.width
                this.largeHeight = titleBmpStart.height
                this.largeWidth = titleBmpStart.width
            }
        }
    }

    /**
     * The "standard" animate method. An optional boolean flag can specify text animation being
     * done by resizing a bitmap or using textSize.
     */
    fun animate(slideOffset: Float, mainActivity: MainActivity) {
        // Because we use ConstraintLayout.LayoutParams to move Next/Previous, and they are altered
        // when we apply ConstraintSets, we need to apply Constraints first and do LayoutParams after.
        slideViewsUsingConstraintBias(slideOffset, mainActivity)
        scaleTextViewWithBitmap(slideOffset, mainActivity)
        fadeAndSlideNextPrevious(slideOffset, mainActivity)
        scalePlayButton(slideOffset, mainActivity)

        mainActivity.soundsText.alpha = slideOffset
        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_START + (CHEVRON_ROTATION_SCALE * slideOffset)
    }

    /** Sets all views to the state they should be in when the drawer is closed. */
    fun drawerOpen(mainActivity: MainActivity) {
        // Because we use ConstraintLayout.LayoutParams to move Next/Previous, and they are altered
        // when we apply ConstraintSets, we need to apply Constraints first and do LayoutParams after.
        if (!durationConnectedToText)
            connectDuration(mainActivity)
        slideViewsUsingConstraintBias(SLIDE_OFFSET_EXPANDED, mainActivity)
        fadeAndSlideNextPrevious(SLIDE_OFFSET_EXPANDED, mainActivity)
        scalePlayButton(SLIDE_OFFSET_EXPANDED, mainActivity)

        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_SIZE_END)
        mainActivity.soundsText.alpha = ALPHA_END
        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_END

        // Ensure the Title TextView is visible and its Bitmap ImageView is invisible so RecyclerView changes update correctly.
        if (mainActivity.songTitle.visibility == View.INVISIBLE)
            mainActivity.songTitle.visibility = View.VISIBLE
        if (mainActivity.songTitleBmp.visibility == View.VISIBLE)
            mainActivity.songTitleBmp.visibility = View.INVISIBLE
    }

    /** Sets all views to the state they should be in when the drawer is open. */
    fun drawerClosed(mainActivity: MainActivity) {
        // Because we use ConstraintLayout.LayoutParams to move Next/Previous, and they are altered
        // when we apply ConstraintSets, we need to apply Constraints first and do LayoutParams after.
        if (!durationConnectedToText)
            connectDuration(mainActivity)
        slideViewsUsingConstraintBias(SLIDE_OFFSET_COLLAPSED, mainActivity)
        fadeAndSlideNextPrevious(SLIDE_OFFSET_COLLAPSED, mainActivity)
        scalePlayButton(SLIDE_OFFSET_COLLAPSED, mainActivity)

        mainActivity.soundsText.alpha = ALPHA_START
        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_SIZE_START)
        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_START

        // Ensure the Title TextView is visible and its Bitmap ImageView is invisible so RecyclerView changes update correctly.
        if (mainActivity.songTitle.visibility == View.INVISIBLE)
            mainActivity.songTitle.visibility = View.VISIBLE
        if (mainActivity.songTitleBmp.visibility == View.VISIBLE)
            mainActivity.songTitleBmp.visibility = View.INVISIBLE
    }

    /** Instead of resizing the text using textSize, we resize bitmap images for a smoother animation. */
    private fun scaleTextViewWithBitmap(slideOffset: Float, mainActivity: MainActivity) {
        // Ensure the TextView is invisible and the Bitmap ImageView is visible.
        mainActivity.songTitle.visibility = View.INVISIBLE
        val songTitleBmp = mainActivity.songTitleBmp
        songTitleBmp.visibility = View.VISIBLE

        // Wait to swap bitmaps to avoid kerning errors. Use the larger bitmap the majority of the time to avoid pixelation.
        if (isOpening) {
            songTitleBmp.setImageBitmap(if (slideOffset < 0.2f) this.titleBmpStart else this.titleBmpEnd)
        } else {
            songTitleBmp.setImageBitmap(if (slideOffset > 0.2f) this.titleBmpStart else this.titleBmpEnd)
        }

        // Towards the start / end of the slide animation, set the size to WRAP_CONTENT to avoid resizing hiccups.
        val tmpOffset = -0.05f + (slideOffset / 0.90f)
        if (tmpOffset <= 0.01f || tmpOffset >= 0.99f) {
            songTitleBmp.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            songTitleBmp.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            songTitleBmp.layoutParams.width = smallWidth + ((largeWidth - smallWidth) * tmpOffset).toInt()
            songTitleBmp.layoutParams.height = smallHeight + ((largeHeight - smallHeight) * tmpOffset).toInt()
        }


        // Because changing constraints programmatically requires creation of ConstraintSets, we
        // want to do it as infrequently as possible. A flag is more barbaric, but more efficient.
        if (durationConnectedToText)
            connectDuration(mainActivity, true)
    }

    /** Slide the playButtonMain, songTitleMain, and songDurationMain using horizontal and vertical constraint bias. */
    private fun slideViewsUsingConstraintBias(slideOffset: Float, mainActivity: MainActivity) {
        val cs = ConstraintSet()
        cs.clone(mainActivity.mainPanel)
        // Slide the play button, song duration, song title, and song title bitmap into their places.
        cs.setVerticalBias(R.id.playButton, (PLAY_VERTICAL_BIAS_START + (slideOffset * PLAY_VERTICAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.playButton, (PLAY_HORIZONTAL_BIAS_START - (slideOffset * PLAY_HORIZONTAL_BIAS_SCALE)))
        cs.setVerticalBias(R.id.songTitle, (TITLE_VERTICAL_BIAS_START + (slideOffset * TITLE_VERTICAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.songTitle, (TITLE_HORIZONTAL_BIAS_START + (slideOffset * TITLE_HORIZONTAL_BIAS_SCALE)))
        cs.setVerticalBias(R.id.songTitleBmp, (TITLE_VERTICAL_BIAS_START + (slideOffset * TITLE_VERTICAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.songTitleBmp, (TITLE_HORIZONTAL_BIAS_START + (slideOffset * TITLE_HORIZONTAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.songDuration, (DURATION_HORIZONTAL_BIAS_START + (slideOffset * DURATION_HORIZONTAL_BIAS_SCALE)))
        cs.applyTo(mainActivity.mainPanel)
    }

    /** Fade in the next and previous buttons and slide them away from the play button. */
    private fun fadeAndSlideNextPrevious(slideOffset: Float, mainActivity: MainActivity) {
        val margin = if (slideOffset == SLIDE_OFFSET_COLLAPSED) 0 else dpToPx((50 * slideOffset), mainActivity)
        // Fade in the previous button and slowly move it further from the play button.
        val previousMain = mainActivity.previous
        previousMain.alpha = slideOffset
        val marginEnd = previousMain.layoutParams as ConstraintLayout.LayoutParams
        marginEnd.rightMargin = margin
        mainActivity.mainPanel.updateViewLayout(previousMain, marginEnd)

        // Fade in the next button and slowly move it further from the play button.
        val nextMain = mainActivity.next
        nextMain.alpha = slideOffset
        val marginStart = nextMain.layoutParams as ConstraintLayout.LayoutParams
        marginStart.leftMargin = margin
        mainActivity.mainPanel.updateViewLayout(nextMain, marginStart)
    }

    /** Scale the play button so it is larger when the drawer opens and smaller when closed. */
    private fun scalePlayButton(slideOffset: Float, mainActivity: MainActivity) {
        // Increase the size of the play button
        val params = mainActivity.playButton.layoutParams
        val side = dpToPx((PLAY_SIDE_DP_START + (slideOffset * PLAY_SIDE_DP_SCALE)), mainActivity)
        params.width = side
        params.height = side
    }

    /**
     * Because we use an ImageView for the bitmap animation strategy, we need to change songDuration's
     * constraints to either the bitmap or the TextView depending on the strategy chosen.
     */
    private fun connectDuration(mainActivity: MainActivity, toBmp: Boolean = false) {
        val cs = ConstraintSet()
        val connection = if (toBmp) R.id.songTitleBmp else R.id.songTitle
        this.durationConnectedToText = !toBmp
        cs.clone(mainActivity.mainPanel)
        cs.clear(R.id.songDuration, ConstraintSet.TOP)
        cs.clear(R.id.songDuration, ConstraintSet.END)
        cs.clear(R.id.songDuration, ConstraintSet.START)
        cs.connect(R.id.songDuration, ConstraintSet.TOP, connection, ConstraintSet.BOTTOM)
        cs.connect(R.id.songDuration, ConstraintSet.END, connection, ConstraintSet.END)
        cs.connect(R.id.songDuration, ConstraintSet.START, connection, ConstraintSet.START)
        cs.applyTo(mainActivity.mainPanel)
    }

    /** Converts a dp measurement (Float) into pixels (Int) */
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }
}