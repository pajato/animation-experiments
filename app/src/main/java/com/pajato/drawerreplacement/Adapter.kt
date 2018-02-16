package com.pajato.drawerreplacement

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_view.view.*

class Adapter : RecyclerView.Adapter<Adapter.VH>() {
    private var items = listOf(R.drawable.gradient_blue,
            R.drawable.gradient_green,
            R.drawable.gradient_red)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.layout.item_background.setImageResource(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return VH(v)
    }


    class VH(val layout: View) : RecyclerView.ViewHolder(layout)
}