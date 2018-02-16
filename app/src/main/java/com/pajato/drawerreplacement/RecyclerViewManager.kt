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
        layout.songTitleMain.text = when (pos) {
            0 -> "Blue Songs!"
            1 -> "Green Songs!"
            2 -> "Red Songs!"
            else -> layout.context.resources.getString(R.string.song_title)
        }
        layout.songDurationMain.text = when (pos) {
            0 -> "Playing until you've cheered up..."
            1 -> "Playing until you're satisfied..."
            2 -> "Playing until you've calmed down..."
            else -> layout.context.resources.getString(R.string.playing_all_night)
        }
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
