package com.pajato.drawerreplacement

object Dimens {
    var SLIDE_OFFSET_COLLAPSED: Float = 0.0f
    var SLIDE_OFFSET_EXPANDED: Float = 1.0f

    var PLAY_VERTICAL_BIAS_START: Float = 0.015f
    private var PLAY_VERTICAL_BIAS_END: Float = 0.75f
    var PLAY_VERTICAL_BIAS_SCALE: Float = 0.735f // PLAY_VERTICAL_BIAS_END - PLAY_VERTICAL_BIAS_START
    var PLAY_HORIZONTAL_BIAS_START: Float = 0.98f
    private var PLAY_HORIZONTAL_BIAS_END: Float = 0.5f
    var PLAY_HORIZONTAL_BIAS_SCALE: Float = 0.48f // PLAY_HORIZONTAL_BIAS_START - PLAY_HORIZONTAL_BIAS_END
    var PLAY_SIDE_DP_START: Float = 48.0f
    private var PLAY_SIDE_DP_END: Float = 72.0f
    var PLAY_SIDE_DP_SCALE: Float = 24.0f // PLAY_SIDE_DP_END - PLAY_SIDE_DP_START

    var TITLE_VERTICAL_BIAS_START: Float = 0.017f
    private var TITLE_VERTICAL_BIAS_END: Float = 0.33f
    var TITLE_VERTICAL_BIAS_SCALE: Float = 0.313f // TITLE_VERTICAL_BIAS_END - TITLE_VERTICAL_BIAS_START
    var TITLE_HORIZONTAL_BIAS_START: Float = 0.05f
    private var TITLE_HORIZONTAL_BIAS_END: Float = 0.5f
    var TITLE_HORIZONTAL_BIAS_SCALE: Float = 0.45f // TITLE_HORIZONTAL_BIAS_END - TITLE_HORIZONTAL_BIAS_START
    var TITLE_SIZE_START: Float = 16.0f
    var TITLE_SIZE_END: Float = 36.0f
    var TITLE_SIZE_SCALE: Float = 20.0f // TITLE_SIZE_END - TITLE_SIZE_START
    var TITLE_SIZE_SCALE_COMPACT: Float = 30.0f // TITLE_SIZE_SCALE * 1.5f

    var DURATION_HORIZONTAL_BIAS_START: Float = 0.0f
    private var DURATION_HORIZONTAL_BIAS_END: Float = 0.5f
    var DURATION_HORIZONTAL_BIAS_SCALE: Float = 0.5f // DURATION_HORIZONTAL_BIAS_END - DURATION_HORIZONTAL_BIAS_START
    var DURATION_SIZE_START: Float = 14.0f
    var DURATION_SIZE_END: Float = 18.0f
    var DURATION_SIZE_SCALE: Float = 4.0f // DURATION_SIZE_END - DURATION_SIZE_START
    var DURATION_SIZE_SCALE_COMPACT: Float = 6.0f // DURATION_SIZE_SCALE * 1.5f

    var CHEVRON_ROTATION_START: Float = 270.0f
    var CHEVRON_ROTATION_END: Float = 450.0f
    var CHEVRON_ROTATION_SCALE: Float = 180.0f // CHEVRON_ROTATION_END - CHEVRON_ROTATION_START

    var ALPHA_START: Float = 0.0f
    var ALPHA_END: Float = 1.0f

    /** Because we are using only 2/3rds of the slide offset, we need to scale the offset by 3/2, so 1.50f. */
    var SPLIT_SCALE: Float = 1.50f
    var SPLIT_TEXT_END: Float = 0.67f
    var SPLIT_SLIDE_START: Float = 0.33f

    fun init(mainActivity: MainActivity) {
        this.SLIDE_OFFSET_COLLAPSED = mainActivity.getDimen(R.dimen.SLIDE_OFFSET_COLLAPSED)
        this.SLIDE_OFFSET_EXPANDED = mainActivity.getDimen(R.dimen.SLIDE_OFFSET_EXPANDED)

        this.PLAY_VERTICAL_BIAS_START = mainActivity.getDimen(R.dimen.PLAY_VERTICAL_BIAS_START)
        this.PLAY_VERTICAL_BIAS_END = mainActivity.getDimen(R.dimen.PLAY_VERTICAL_BIAS_END)
        this.PLAY_VERTICAL_BIAS_SCALE = mainActivity.getDimen(R.dimen.PLAY_VERTICAL_BIAS_SCALE)
        this.PLAY_HORIZONTAL_BIAS_START = mainActivity.getDimen(R.dimen.PLAY_HORIZONTAL_BIAS_START)
        this.PLAY_HORIZONTAL_BIAS_END = mainActivity.getDimen(R.dimen.PLAY_HORIZONTAL_BIAS_END)
        this.PLAY_HORIZONTAL_BIAS_SCALE = mainActivity.getDimen(R.dimen.PLAY_HORIZONTAL_BIAS_SCALE)
        this.PLAY_SIDE_DP_START = mainActivity.getDimen(R.dimen.PLAY_SIDE_DP_START)
        this.PLAY_SIDE_DP_END = mainActivity.getDimen(R.dimen.PLAY_SIDE_DP_END)
        this.PLAY_SIDE_DP_SCALE = mainActivity.getDimen(R.dimen.PLAY_SIDE_DP_SCALE)

        this.TITLE_VERTICAL_BIAS_START = mainActivity.getDimen(R.dimen.TITLE_VERTICAL_BIAS_START)
        this.TITLE_VERTICAL_BIAS_END = mainActivity.getDimen(R.dimen.TITLE_VERTICAL_BIAS_END)
        this.TITLE_VERTICAL_BIAS_SCALE = mainActivity.getDimen(R.dimen.TITLE_VERTICAL_BIAS_SCALE)
        this.TITLE_HORIZONTAL_BIAS_START = mainActivity.getDimen(R.dimen.TITLE_HORIZONTAL_BIAS_START)
        this.TITLE_HORIZONTAL_BIAS_END = mainActivity.getDimen(R.dimen.TITLE_HORIZONTAL_BIAS_END)
        this.TITLE_HORIZONTAL_BIAS_SCALE = mainActivity.getDimen(R.dimen.TITLE_HORIZONTAL_BIAS_SCALE)
        this.TITLE_SIZE_START = mainActivity.getDimen(R.dimen.TITLE_SIZE_START)
        this.TITLE_SIZE_END = mainActivity.getDimen(R.dimen.TITLE_SIZE_END)
        this.TITLE_SIZE_SCALE = mainActivity.getDimen(R.dimen.TITLE_SIZE_SCALE)
        this.TITLE_SIZE_SCALE_COMPACT = mainActivity.getDimen(R.dimen.TITLE_SIZE_SCALE_COMPACT)

        this.DURATION_HORIZONTAL_BIAS_START = mainActivity.getDimen(R.dimen.DURATION_HORIZONTAL_BIAS_START)
        this.DURATION_HORIZONTAL_BIAS_END = mainActivity.getDimen(R.dimen.DURATION_HORIZONTAL_BIAS_END)
        this.DURATION_HORIZONTAL_BIAS_SCALE = mainActivity.getDimen(R.dimen.DURATION_HORIZONTAL_BIAS_SCALE)
        this.DURATION_SIZE_START = mainActivity.getDimen(R.dimen.DURATION_SIZE_START)
        this.DURATION_SIZE_END = mainActivity.getDimen(R.dimen.DURATION_SIZE_END)
        this.DURATION_SIZE_SCALE = mainActivity.getDimen(R.dimen.DURATION_SIZE_SCALE)
        this.DURATION_SIZE_SCALE_COMPACT = mainActivity.getDimen(R.dimen.DURATION_SIZE_SCALE_COMPACT)

        this.CHEVRON_ROTATION_START = mainActivity.getDimen(R.dimen.CHEVRON_ROTATION_START)
        this.CHEVRON_ROTATION_END = mainActivity.getDimen(R.dimen.CHEVRON_ROTATION_END)
        this.CHEVRON_ROTATION_SCALE = mainActivity.getDimen(R.dimen.CHEVRON_ROTATION_SCALE)

        this.ALPHA_START = mainActivity.getDimen(R.dimen.ALPHA_START)
        this.ALPHA_END = mainActivity.getDimen(R.dimen.ALPHA_END)

        this.SPLIT_SCALE = mainActivity.getDimen(R.dimen.SPLIT_SCALE)
        this.SPLIT_TEXT_END = mainActivity.getDimen(R.dimen.SPLIT_TEXT_END)
        this.SPLIT_SLIDE_START = mainActivity.getDimen(R.dimen.SPLIT_SLIDE_START)
    }
}