package com.pajato.drawerreplacement

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_drawer.view.*

class RecyclerViewManager(private val layout: ConstraintLayout, val adapter: Adapter, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    /** Handle setting the visibility of the next and previous buttons. */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val newPos = if (dx > 0) {
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        } else {
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        layout.nextMain.visibility = if (newPos == recyclerView.adapter.itemCount - 1) INVISIBLE else VISIBLE
        layout.previousMain.visibility = if (newPos == 0) INVISIBLE else VISIBLE
        updateSongInformation(newPos)
    }

    fun updateSongInformation(pos: Int) {
        val resIdTitle = when (pos) {
            0 -> R.string.blueSongs
            1 -> R.string.greenSongs
            2 -> R.string.redSongs
            else -> R.string.song_title
        }
        layout.songTitleMain.setText(resIdTitle)

        val resIdDuration = when (pos) {
            0 -> R.string.blueDuration
            1 -> R.string.greenDuration
            2 -> R.string.redDuration
            else -> R.string.playing_all_night
        }
        layout.songDurationMain.setText(resIdDuration)
    }

    fun changeSong(view: View) {
        var pos = layoutManager.findFirstCompletelyVisibleItemPosition()
        val smoothScroller = SmoothScroller(layout.context)
        pos = when (view.id) {
            R.id.nextMain -> pos + 1
            R.id.previousMain -> pos - 1
            else -> pos
        }
        smoothScroller.targetPosition = pos
        updateSongInformation(pos)
        layoutManager.startSmoothScroll(smoothScroller)
    }

}
