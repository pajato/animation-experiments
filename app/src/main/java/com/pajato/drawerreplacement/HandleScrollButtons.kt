package com.pajato.drawerreplacement

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

class HandleScrollButtons(private val nextMain: View, private val previousMain: View) : RecyclerView.OnScrollListener() {

    /** Handle setting the visibility of the next and previous buttons. */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val newPos = if (dx > 0) {
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        } else {
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        nextMain.visibility = if (newPos == recyclerView.adapter.itemCount - 1) INVISIBLE else VISIBLE
        previousMain.visibility = if (newPos == 0) INVISIBLE else VISIBLE
    }
}
