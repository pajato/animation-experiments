package com.pajato.drawerreplacement

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.content_drawer.*

class RecyclerViewManager(private val activity: MainActivity, val adapter: Adapter, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    /** Handle setting the visibility of the next and previous buttons. */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val newPos = if (dx > 0) {
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        } else {
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        activity.next.visibility = if (newPos == recyclerView.adapter.itemCount - 1) INVISIBLE else VISIBLE
        activity.previous.visibility = if (newPos == 0) INVISIBLE else VISIBLE
        updateSongInformation(newPos)
    }

    fun updateSongInformation(pos: Int) {
        val resIdTitle = when (pos) {
            0 -> R.string.blueSongs
            1 -> R.string.greenSongs
            2 -> R.string.redSongs
            else -> R.string.song_title
        }
        activity.songTitle.setText(resIdTitle)
        activity.invisTitle.setText(resIdTitle)

        val resIdDuration = when (pos) {
            0 -> R.string.blueDuration
            1 -> R.string.greenDuration
            2 -> R.string.redDuration
            else -> R.string.playing_all_night
        }
        activity.songDuration.setText(resIdDuration)
    }

    fun changeSong(view: View) {
        var pos = layoutManager.findFirstCompletelyVisibleItemPosition()
        val smoothScroller = SmoothScroller(activity)
        pos = when (view.id) {
            R.id.next -> pos + 1
            R.id.previous -> pos - 1
            else -> pos
        }
        smoothScroller.targetPosition = pos
        layoutManager.startSmoothScroll(smoothScroller)
    }

}
