package com.pajato.drawerreplacement

import android.content.Context
import android.support.v7.widget.LinearSmoothScroller

class SmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun getVerticalSnapPreference(): Int {
        return LinearSmoothScroller.SNAP_TO_START
    }
}