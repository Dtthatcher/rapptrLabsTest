package com.datechnologies.androidtest.game


import android.media.MediaPlayer
import android.widget.ImageView


open class Simon {

    private val colorList = listOf(Colors.RED, Colors.BLUE, Colors.GREEN, Colors.YELLOW)

    val currentPattern = mutableListOf<Colors>()
    val playerChoices = mutableListOf<Colors>()

    var roundNumber = 0
    var totalScore = 0
    var count = 0
    var index = 0


    fun addColorToPattern() {
        val randomNum = Math.random() * colorList.size
            currentPattern.add(colorList[randomNum.toInt()])
    }

    fun addColorToPlayerChoice(color: Colors) {
        playerChoices.add(color)
    }

    fun checkForCorrect(index: Int): Boolean {
        return playerChoices[index] == currentPattern[index]
    }

    fun animateImage(imageView: ImageView, sound: MediaPlayer) {

        imageView.animate()
            .setDuration(1300)
            .alpha(1F)
            .withStartAction { if(!sound.isPlaying) sound.start(); else sound.start() }
            .withEndAction { imageView.alpha = .5F }
    }
}

enum class Colors() {
    RED,
    BLUE,
    GREEN,
    YELLOW
}
