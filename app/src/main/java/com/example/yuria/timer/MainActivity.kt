package com.example.yuria.timer

import android.icu.util.DateInterval
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    inner class MyCountDownTimer(millisInFuture: Long,
                                 countDownInterval: Long) :
    CountDownTimer(millisInFuture, countDownInterval) {
        var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%1d:%2$02d".format(minute, second)
        }

        override fun onFinish() {
            timerText.text = "0:00"
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "3:00"
        val timer = MyCountDownTimer(3 * 60 * 1000, 100)
        playStop.setOnClickListener {
            when (timer.isRunning) {
                true -> timer.apply {
                    isRunning = false
                    cancel()
                    playStop.setImageResource(
                            R.drawable.ic_play_arrow_black_24dp)
                }
                false -> timer.apply {
                    isRunning = true
                    start()
                    playStop.setImageResource(
                            R.drawable.ic_stop_black_24dp)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        soundPool =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    @Suppress("DEPRECATION")
                    SoundPool(2,AudioManager.STREAM_ALARM, 0)
                } else {
                    val audioAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build()
                    SoundPool.Builder()
                            .setMaxStreams(1)
                            .setAudioAttributes(audioAttributes)
                            .build()
                }
        soundResId = soundPool.load(this, R.raw.bellsound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}
