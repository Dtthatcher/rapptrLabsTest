package com.datechnologies.androidtest.view.animation

import android.content.*
import android.os.Bundle
import android.os.Build

import com.datechnologies.androidtest.R
import com.datechnologies.androidtest.view.main.MainActivity
//drag shadow imports
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import androidx.core.content.res.ResourcesCompat
import android.view.View
//drag view listener imports
import android.media.MediaPlayer
import android.view.DragEvent

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.databinding.DataBindingUtil

import com.datechnologies.androidtest.databinding.ActivityAnimationBinding
import com.datechnologies.androidtest.game.Colors
import com.datechnologies.androidtest.game.Simon

import tyrantgit.explosionfield.ExplosionField

import java.util.*
import kotlin.concurrent.schedule

private class LogoShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    private val shadow = ResourcesCompat.getDrawable(view.context.resources, R.drawable.gpc_logo, view.context.theme)

    override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
        val width: Int = view.width
        val height: Int = view.height

        shadow?.setBounds(0,0,width,height)
        outShadowSize.set(width, height)
        outShadowTouchPoint.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {

        shadow?.draw(canvas)
    }
}



/**
 * Screen that displays the D & A Technologies logo.
 * The icon can be moved around on the screen as well as animated.
 */
class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBinding
    private lateinit var pop: MediaPlayer
    private lateinit var robot: MediaPlayer
    private lateinit var cork: MediaPlayer
    private lateinit var ding: MediaPlayer
    private lateinit var gameOver: MediaPlayer
    private lateinit var exp: ExplosionField
    private lateinit var logo: ImageView
    private lateinit var animationBackground: ConstraintLayout
    private lateinit var gameStart: ConstraintLayout
    private lateinit var gameStartTextBox:TextView
    private lateinit var button: Button
    private lateinit var red: ImageView
    private lateinit var blue: ImageView
    private lateinit var green: ImageView
    private lateinit var yellow: ImageView

    val game = Game()
    val playRed = { game.animateImage(binding.red, pop) }
    val playBlue = { game.animateImage(binding.blue, robot) }
    val playGreen = { game.animateImage(binding.green, cork) }
    val playYellow = { game.animateImage(binding.yellow, ding) }


    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_animation)
        animationBackground = binding.animationBackground
        logo = binding.logo
        gameStart = binding.gameStart
        gameStartTextBox = binding.gameStartTextBox
        button = binding.button
        red = binding.red
        blue = binding.blue
        green = binding.green
        yellow = binding.yellow

        animationBackground.setOnDragListener(logoDragListener)
        gameStart.setOnDragListener(logoDragListener)

        println(animationBackground.y.toInt().toString() + "height")
        println(animationBackground.maxHeight)


        /** Sounds for game class **/
        exp = ExplosionField.attach2Window(this)
        pop = MediaPlayer.create(this, R.raw.pop)
        robot = MediaPlayer.create(this, R.raw.robot_blip)
        cork = MediaPlayer.create(this, R.raw.cork_pop)
        ding = MediaPlayer.create(this, R.raw.ding)
        gameOver = MediaPlayer.create(this, R.raw.game_over)

        button.setOnClickListener { fadeOutLogo() }
        red.setOnClickListener {
            if (!pop.isPlaying) playRed()
            else playRed()
            game.addColorToPlayerChoice(Colors.RED)
            game.playerTurn()
        }
        blue.setOnClickListener {
            if(!robot.isPlaying) playBlue()
            else playBlue()
            game.addColorToPlayerChoice(Colors.BLUE)
            game.playerTurn()
        }
        green.setOnClickListener {
            if(!cork.isPlaying) playGreen()
            else playGreen()
            game.addColorToPlayerChoice(Colors.GREEN)
            game.playerTurn()
        }
        yellow.setOnClickListener {
            if(!ding.isPlaying) playYellow()
            else playYellow()
            game.addColorToPlayerChoice(Colors.YELLOW)
            game.playerTurn()
        }

        attachViewDragListener()

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation. Done
        // TODO: Add a ripple effect when the buttons are clicked Done

        // TODO: When the fade button is clicked, you must animate the D & A Technologies logo. DONE
        // TODO: It should fade from 100% alpha to 0% alpha, and then from 0% alpha to 100% alpha DONE

        // TODO: The user should be able to touch and drag the D & A Technologies logo around the screen. DONE

        // TODO: Add a bonus to make yourself stick out. Music, color, fireworks, explosions!!! DONE
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /** attach on long click listener to logo and build shadow for drag **/
    private fun attachViewDragListener() {

        logo.setOnLongClickListener { view: View ->

            val tag = logo.id.toString()
            val item = ClipData.Item(tag)
            val dataToDrag = ClipData(
                tag,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            val logoShadow = LogoShadowBuilder(view)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, logoShadow, view, 0)
            } else {
                view.startDragAndDrop(dataToDrag, logoShadow, view, 0)
            }

            view.visibility = View.INVISIBLE
            true
        }
    }
/** Drag and Drop Listener Actions **/
    private val logoDragListener = View.OnDragListener { view, dragEvent ->
        val startingX = logo.x
        val startingY = logo.y

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                if (view == binding.gameStart){
                    gameStartTextBox.setTextColor(Color.GREEN)
                }
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                if (view == gameStart){
                    gameStartTextBox.setTextColor(Color.RED)
                }
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val draggableItem = dragEvent.localState as View

                val parent = draggableItem.parent as ViewGroup
                parent.removeView(draggableItem)
                val originalY = draggableItem.y

                draggableItem.x = dragEvent.x - (draggableItem.width / 2)
                draggableItem.y = dragEvent.y - (draggableItem.height / 2)


                if (view == gameStart) {
                    val dropArea = animationBackground
                    dropArea.removeViewInLayout(view)
                    dropArea.addView(draggableItem)
                    draggableItem.visibility = View.VISIBLE
                } else {
                    val dropArea = view as ConstraintLayout
                    val backgroundY = animationBackground.y.toInt()
                    if (draggableItem.y.toInt() - 30 in (backgroundY - 200)..(backgroundY + 100)) draggableItem.y = startingY
                    dropArea.addView(draggableItem)
                    draggableItem.visibility = View.VISIBLE
                }

                if (view == gameStart) checkIfInGameTextArea(dragEvent, draggableItem, startingX)

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }
    }



    private fun checkIfInGameTextArea(dragEvent: DragEvent, item: View, start: Float){
        val gameTextAreaXStart = gameStartTextBox.x
        val gameTextAreaYStart = gameStartTextBox.y

        val gameTextAreaXEnd = gameTextAreaXStart + gameStartTextBox.width
        val gameTextAreaYEnd = gameTextAreaYStart + gameStartTextBox.height

        if (dragEvent.x in gameTextAreaXStart..gameTextAreaXEnd && dragEvent.y in gameTextAreaYStart..gameTextAreaYEnd) {
            item.isClickable = false
            item.x = start
            item.y = binding.guidelineMiddle.y - (item.height / 2)
            game.playGame()
        }

    }

/**  Functions to fade logo in and out **/

    private fun fadeOutLogo(){
        logo.animate().setDuration(1500).alpha(0F)
        button.setText(R.string.fade_in)
        button.setOnClickListener { fadeInLogo() }
    }

    private fun fadeInLogo(){
        logo.animate().setDuration(1500).alpha(1F)
        button.setText(R.string.fade_out)
        button.setOnClickListener { fadeOutLogo() }
    }

    /****************************************************************************************************
    * Simple simon game. Follow along to see how far you can make it!
    *
    * ***************************************************************************************************/

    inner class Game : Simon() {

        private fun fadeInGame(){
            red.animate().setDuration(1000).alpha(.3F)
            blue.animate().setDuration(1000).alpha(.3F)
            green.animate().setDuration(1000).alpha(.3F)
            yellow.animate().setDuration(1000).alpha(.3F)

        }

        private fun computerTurn(){

            addColorToPattern()
            roundNumber++
            var time = 0L
            val timeList = mutableListOf<Long>()
            for ((index, color) in currentPattern.withIndex()) {
                time += 1300L
                timeList.add(time)
                val delay = timeList[index]
                when (color){
                    Colors.RED -> Timer().schedule(delay) { this@AnimationActivity.runOnUiThread(playRed) }
                    Colors.BLUE -> Timer().schedule(delay) { this@AnimationActivity.runOnUiThread(playBlue) }
                    Colors.GREEN -> Timer().schedule(delay) { this@AnimationActivity.runOnUiThread(playGreen) }
                    Colors.YELLOW -> Timer().schedule(delay) { this@AnimationActivity.runOnUiThread(playYellow) }
                }
            }
        }

        fun playerTurn () {
            count ++
            index = count - 1
            if (!checkForCorrect(index)) lose()
            else if (count == game.currentPattern.size) {
                count = 0
                index = 0
                totalScore ++
                playerChoices.clear()
                computerTurn()
            } else totalScore++
        }

        fun playGame(){
            fadeInGame()
            gameStart.visibility = View.INVISIBLE
            red.visibility = View.VISIBLE
            blue.visibility = View.VISIBLE
            green.visibility = View.VISIBLE
            yellow.visibility = View.VISIBLE

            Timer().schedule(2000) { computerTurn() }
        }

        private fun lose () {
                val dialogBuilder = AlertDialog.Builder(animationBackground.context)
                val finalScore = totalScore
                val round = roundNumber
                gameOver.start()
                dialogBuilder.setMessage("""
                    YOU LOSE!!!
                    
                    Final Score: $finalScore
                    Round Number: $round
                """.trimIndent())
                    .setCancelable(false)
                    .setPositiveButton("Back To Main Screen", { dialog, id ->
                        exp.explode(animationBackground).also { pop.start() }
                        Timer().schedule(1000) { finish() }
                     })

                val alert = dialogBuilder.create()
                alert.setTitle("Lose Alert")
                alert.show()
        }
    }


    companion object {

        fun start(context: Context) {
            val starter = Intent(context, AnimationActivity::class.java)
            context.startActivity(starter)
        }
    }
}