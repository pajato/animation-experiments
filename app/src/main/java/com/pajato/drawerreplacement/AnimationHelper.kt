package com.pajato.drawerreplacement

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.TypedValue
import com.pajato.drawerreplacement.Dimens.ALPHA_END
import com.pajato.drawerreplacement.Dimens.ALPHA_START
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_END
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_SCALE
import com.pajato.drawerreplacement.Dimens.CHEVRON_ROTATION_START
import com.pajato.drawerreplacement.Dimens.DURATION_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.DURATION_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.DURATION_SIZE_END
import com.pajato.drawerreplacement.Dimens.DURATION_SIZE_SCALE
import com.pajato.drawerreplacement.Dimens.DURATION_SIZE_SCALE_COMPACT
import com.pajato.drawerreplacement.Dimens.DURATION_SIZE_START
import com.pajato.drawerreplacement.Dimens.PLAY_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.PLAY_SIDE_DP_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_SIDE_DP_START
import com.pajato.drawerreplacement.Dimens.PLAY_VERTICAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.PLAY_VERTICAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.SLIDE_OFFSET_COLLAPSED
import com.pajato.drawerreplacement.Dimens.SLIDE_OFFSET_EXPANDED
import com.pajato.drawerreplacement.Dimens.SPLIT_SCALE
import com.pajato.drawerreplacement.Dimens.SPLIT_SLIDE_START
import com.pajato.drawerreplacement.Dimens.SPLIT_TEXT_END
import com.pajato.drawerreplacement.Dimens.TITLE_HORIZONTAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.TITLE_HORIZONTAL_BIAS_START
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_END
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_SCALE
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_SCALE_COMPACT
import com.pajato.drawerreplacement.Dimens.TITLE_SIZE_START
import com.pajato.drawerreplacement.Dimens.TITLE_VERTICAL_BIAS_SCALE
import com.pajato.drawerreplacement.Dimens.TITLE_VERTICAL_BIAS_START
import kotlinx.android.synthetic.main.content_drawer.*

object AnimationHelper {

    /**
     * An animation method that splits the text sizing and the shifting and fading of the layouts
     * into two discrete chunks with two discrete scales.
     */
    fun splitAnimate(slideOffset: Float, mainActivity: MainActivity) {
        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_START + (CHEVRON_ROTATION_SCALE * slideOffset)
        // Handle scaling the TextViews. They should be their full size well before the slide offset hits 1.0
        if (slideOffset < SPLIT_TEXT_END) {
            val tmpOffset = slideOffset * SPLIT_SCALE
            compactScaleTextViews(tmpOffset, mainActivity)
            // scaleTextViews(tmpOffset, mainActivity)
        } else {
            mainActivity.songTitle.textSize = TITLE_SIZE_END
            mainActivity.songDuration.textSize = DURATION_SIZE_END
        }

        val soundsTextMain = mainActivity.soundsText
        val playButtonMain = mainActivity.playButton
        if (slideOffset > SPLIT_SLIDE_START) {
            val tmpOffset = (slideOffset - SPLIT_SLIDE_START) * SPLIT_SCALE

            slideViewsUsingConstraintBias(tmpOffset, mainActivity)
            fadeAndSlideNextPrevious(tmpOffset, mainActivity)
            scalePlayButton(slideOffset, mainActivity)

            soundsTextMain.alpha = tmpOffset

        } else {
            val mainPanel = mainActivity.mainPanel
            val cs = ConstraintSet()
            cs.clone(mainPanel)
            // Slide the play button, song duration, and song title into the right place
            cs.setVerticalBias(R.id.playButton, PLAY_VERTICAL_BIAS_START)
            cs.setHorizontalBias(R.id.playButton, PLAY_HORIZONTAL_BIAS_START)
            cs.setVerticalBias(R.id.songTitle, TITLE_VERTICAL_BIAS_START)
            cs.setHorizontalBias(R.id.songTitle, TITLE_HORIZONTAL_BIAS_START)
            cs.setHorizontalBias(R.id.songDuration, DURATION_HORIZONTAL_BIAS_START)
            cs.applyTo(mainPanel)

            // Fade out various views and set the play button's size
            mainActivity.previous.alpha = ALPHA_START
            mainActivity.next.alpha = ALPHA_START
            soundsTextMain.alpha = ALPHA_START
            val params = playButtonMain.layoutParams
            val side = dpToPx(PLAY_SIDE_DP_START, mainActivity)
            params.width = side
            params.height = side
        }
    }

    /** The "standard" animate method that causes stuttering in the TextViews due to constant resizing. */
    fun animate(slideOffset: Float, mainActivity: MainActivity) {
        slideViewsUsingConstraintBias(slideOffset, mainActivity)
        fadeAndSlideNextPrevious(slideOffset, mainActivity)

        mainActivity.soundsText.alpha = slideOffset

        scalePlayButton(slideOffset, mainActivity)
        // compactScaleTextViews(slideOffset, mainActivity)
        scaleTextViews(slideOffset, mainActivity)

        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_START + (CHEVRON_ROTATION_SCALE * slideOffset)
    }

    /** Sets all views to the state they should be in when the drawer is closed. */
    fun drawerOpen(mainActivity: MainActivity) {
        slideViewsUsingConstraintBias(SLIDE_OFFSET_EXPANDED, mainActivity)
        fadeAndSlideNextPrevious(SLIDE_OFFSET_EXPANDED, mainActivity)
        mainActivity.soundsText.alpha = ALPHA_END

        scalePlayButton(SLIDE_OFFSET_EXPANDED, mainActivity)
        mainActivity.songDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, DURATION_SIZE_END)
        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_SIZE_END)

        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_END
    }

    /** Sets all views to the state they should be in when the drawer is open. */
    fun drawerClosed(mainActivity: MainActivity) {
        slideViewsUsingConstraintBias(SLIDE_OFFSET_COLLAPSED, mainActivity)
        fadeAndSlideNextPrevious(SLIDE_OFFSET_COLLAPSED, mainActivity)
        mainActivity.soundsText.alpha = ALPHA_START

        scalePlayButton(SLIDE_OFFSET_COLLAPSED, mainActivity)
        mainActivity.songDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, DURATION_SIZE_START)
        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_SIZE_START)

        mainActivity.chevronClick.rotation = CHEVRON_ROTATION_START
    }

    /** */
    private fun scaleTextViews(slideOffset: Float, mainActivity: MainActivity) {
        val durationSp = DURATION_SIZE_START + (slideOffset * DURATION_SIZE_SCALE)
        mainActivity.songDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, durationSp)

        val titleSp = TITLE_SIZE_START + (slideOffset * TITLE_SIZE_SCALE)
        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSp)
    }

    /** Increase the songDurationMain and songTitleMain TextViews size. */
    private fun compactScaleTextViews(slideOffset: Float, mainActivity: MainActivity) {
        // Increase the text sizes, fade in the "Sounds" textview and rotate the chevron.
        // Ensure that the text size changes occurs more towards the center of the slide to allow for smoother transitions.
        // The commented code is to illustrate what occurs between 0.2 and 0.8 in the slide offset.
        // val durationSp = 14.0f + (slideOffset * 4.0f)
        var durationSp = DURATION_SIZE_START + ((slideOffset - 0.2f) * DURATION_SIZE_SCALE_COMPACT)
        durationSp = when {
            durationSp < DURATION_SIZE_START -> DURATION_SIZE_START
            durationSp > DURATION_SIZE_END -> DURATION_SIZE_END
            else -> durationSp
        }
        mainActivity.songDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, durationSp)
        // val titleSp = 16.0f + (slideOffset * 20.0f)
        var titleSp = TITLE_SIZE_START + ((slideOffset - 0.2f) * TITLE_SIZE_SCALE_COMPACT)
        titleSp = when {
            titleSp < TITLE_SIZE_START -> TITLE_SIZE_START
            titleSp > TITLE_SIZE_END -> TITLE_SIZE_END
            else -> titleSp
        }
        mainActivity.songTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSp)
    }

    /** Slide the playButtonMain, songTitleMain, and songDurationMain using horizontal and vertical constraint bias. */
    private fun slideViewsUsingConstraintBias(slideOffset: Float, mainActivity: MainActivity) {
        val cs = ConstraintSet()
        cs.clone(mainActivity.mainPanel)
        // Slide the play button, song duration, and song title into the right place
        cs.setVerticalBias(R.id.playButton, (PLAY_VERTICAL_BIAS_START + (slideOffset * PLAY_VERTICAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.playButton, (PLAY_HORIZONTAL_BIAS_START - (slideOffset * PLAY_HORIZONTAL_BIAS_SCALE)))
        cs.setVerticalBias(R.id.songTitle, (TITLE_VERTICAL_BIAS_START + (slideOffset * TITLE_VERTICAL_BIAS_SCALE)))
        cs.setHorizontalBias(R.id.songTitle, (TITLE_HORIZONTAL_BIAS_START + (slideOffset * TITLE_HORIZONTAL_BIAS_SCALE)))
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

    /** Converts a dp measurement (Float) into pixels (Int) */
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }
}