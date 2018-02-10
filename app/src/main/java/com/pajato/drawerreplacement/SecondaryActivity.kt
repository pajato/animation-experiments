package com.pajato.drawerreplacement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A copy of the original main activity where I was fiddling with ActivityTransitions and using
 * Transition objects and had opted out of using the SlideUpPanelLayout API. Ultimately scrapped as
 * it did not provide the possibility to drag the
 */
@SuppressLint("Registered")
class SecondaryActivity : AppCompatActivity() {
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun animate(view: View) {
        // reveal views
        isOpen = !isOpen

        sendAnimation(if (isOpen) constraintSetOpen() else constraintSetClosed())

        nextMain.visibility = getVisibility()
        previousMain.visibility = getVisibility()
        seekbarLayoutMain.visibility = getVisibility()
    }

    private fun constraintSetOpen(): ConstraintSet {
        val cs = ConstraintSet()
        cs.clone(parentMain)

        // Center the play button in the view.
        cs.clear(R.id.playButtonMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.playButtonMain, ConstraintSet.END)
        cs.clear(R.id.playButtonMain, ConstraintSet.START)
        cs.clear(R.id.playButtonMain, ConstraintSet.TOP)
        cs.connect(R.id.playButtonMain, ConstraintSet.TOP, R.id.songTitleMain, ConstraintSet.BOTTOM)
        cs.connect(R.id.playButtonMain, ConstraintSet.BOTTOM, R.id.seekbarLayoutMain, ConstraintSet.TOP)
        cs.connect(R.id.playButtonMain, ConstraintSet.START, R.id.parentMain, ConstraintSet.START)
        cs.connect(R.id.playButtonMain, ConstraintSet.END, R.id.parentMain, ConstraintSet.END)

        // Center the title at the top of the layout
        cs.clear(R.id.songTitleMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.songTitleMain, ConstraintSet.END)
        cs.clear(R.id.songTitleMain, ConstraintSet.START)
        cs.clear(R.id.songTitleMain, ConstraintSet.TOP)
        cs.connect(R.id.songTitleMain, ConstraintSet.START, R.id.parentMain, ConstraintSet.START)
        cs.connect(R.id.songTitleMain, ConstraintSet.END, R.id.parentMain, ConstraintSet.END)
        cs.connect(R.id.songTitleMain, ConstraintSet.TOP, R.id.parentMain, ConstraintSet.TOP)

        // Center the background to the center of the layout
        cs.clear(R.id.songImageMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.songImageMain, ConstraintSet.END)
        cs.clear(R.id.songImageMain, ConstraintSet.START)
        cs.clear(R.id.songImageMain, ConstraintSet.TOP)
        cs.connect(R.id.songImageMain, ConstraintSet.TOP, R.id.parentMain, ConstraintSet.BOTTOM)
        cs.connect(R.id.songImageMain, ConstraintSet.BOTTOM, R.id.parentMain, ConstraintSet.TOP)
        cs.connect(R.id.songImageMain, ConstraintSet.START, R.id.parentMain, ConstraintSet.START)
        cs.connect(R.id.songImageMain, ConstraintSet.END, R.id.parentMain, ConstraintSet.END)

        return cs
    }

    private fun constraintSetClosed(): ConstraintSet {
        val cs = ConstraintSet()
        cs.clone(parentMain)

        // Put the play button in the bottom right corner
        cs.clear(R.id.playButtonMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.playButtonMain, ConstraintSet.END)
        cs.clear(R.id.playButtonMain, ConstraintSet.START)
        cs.clear(R.id.playButtonMain, ConstraintSet.TOP)
        cs.connect(R.id.playButtonMain, ConstraintSet.END, R.id.parentMain, ConstraintSet.END)
        cs.connect(R.id.playButtonMain, ConstraintSet.BOTTOM, R.id.parentMain, ConstraintSet.BOTTOM)

        // Put the background in the bottom left corner
        cs.clear(R.id.songImageMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.songImageMain, ConstraintSet.END)
        cs.clear(R.id.songImageMain, ConstraintSet.START)
        cs.clear(R.id.songImageMain, ConstraintSet.TOP)
        cs.connect(R.id.songImageMain, ConstraintSet.START, R.id.parentMain, ConstraintSet.START)
        cs.connect(R.id.songImageMain, ConstraintSet.BOTTOM, R.id.parentMain, ConstraintSet.BOTTOM)

        // Put the title next to the song image
        cs.clear(R.id.songTitleMain, ConstraintSet.BOTTOM)
        cs.clear(R.id.songTitleMain, ConstraintSet.END)
        cs.clear(R.id.songTitleMain, ConstraintSet.START)
        cs.clear(R.id.songTitleMain, ConstraintSet.TOP)
        cs.connect(R.id.songTitleMain, ConstraintSet.START, R.id.songImageMain, ConstraintSet.END)
        cs.connect(R.id.songTitleMain, ConstraintSet.TOP, R.id.songImageMain, ConstraintSet.TOP)
        cs.connect(R.id.songTitleMain, ConstraintSet.BOTTOM, R.id.songImageMain, ConstraintSet.BOTTOM)

        return cs
    }

    private fun sendAnimation(cs: ConstraintSet) {
        TransitionManager.beginDelayedTransition(parentMain)
        cs.applyTo(parentMain)
    }

    private fun getVisibility(): Int {
        return if (isOpen) View.VISIBLE else View.GONE
    }

    fun transitionActivity(view: View) {
        val intent = Intent(this, DrawerActivity::class.java)

        val textPair: Pair<View, String> = Pair.create(this.songTitleMain, "textTransition")
        val imagePair: Pair<View, String> = Pair.create(this.playButtonMain, "slidePlay")
        val backgroundPair: Pair<View, String> = Pair.create(this.songImageMain, "backgroundTransition")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, textPair, imagePair, backgroundPair)
        startActivity(intent, options.toBundle())
    }
}