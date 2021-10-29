package com.example.lab3

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_audio.*
import android.media.AudioManager
import androidx.core.content.ContextCompat


class AudioActivity : AppCompatActivity() {

    private var mp: MediaPlayer? = null
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        val song = myListSong[CurrentSongPosition]

        mp?.reset()
        mp = MediaPlayer.create(this, song.SongURI)
        mp?.isLooping = true
        mp?.setVolume(0.5f, 0.5f)
        totalTime = mp?.duration!!

        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        mp?.setVolume(volumeNum, volumeNum)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp?.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    val msg = Message()
                    msg.what = mp?.currentPosition!!
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val currentPosition = msg.what

            // Update positionBar
            positionBar.progress = currentPosition

            // Update Labels
            val elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            val remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun playBtnClick(v: View) {

        if (mp?.isPlaying == true) {
            // Stop
            mp?.pause()
            playBtn.setBackgroundResource(R.drawable.play)

        } else {
            // Start
            mp?.start()
            playBtn.setBackgroundResource(R.drawable.stop)
        }
    }

    fun onClick(view: View) {
        if (mp == null) return
        when (view.id) {
            R.id.btnBackward -> mp?.getCurrentPosition()?.minus(10000)?.let { mp?.seekTo(it) }
            R.id.btnForward -> mp?.getCurrentPosition()?.plus(10000)?.let { mp?.seekTo(it) }
            R.id.btnBack ->
            {
                val intent = Intent(this@AudioActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btnPrev ->
            {
                if (CurrentSongPosition != 0)
                {
                    CurrentSongPosition--
                    val song = myListSong[CurrentSongPosition]
                    mp?.reset()
                    mp = MediaPlayer.create(this, song.SongURI)
                    mp?.isLooping = true
                    mp?.setVolume(0.5f, 0.5f)
                    totalTime = mp?.duration!!
                }
            }
            R.id.btnNext->
            {
                if (CurrentSongPosition != myListSong.size-1)
                {
                    CurrentSongPosition++
                    val song = myListSong[CurrentSongPosition]
                    mp?.reset()
                    mp = MediaPlayer.create(this, song.SongURI)
                    mp?.isLooping = true
                    mp?.setVolume(0.5f, 0.5f)
                    totalTime = mp?.duration!!
                }
            }

        }
    }

    private fun releaseMP() {
        if (mp != null) {
            try {
                mp!!.release()
                mp = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStop(){
        super.onStop()
        releaseMP()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMP()
    }
}