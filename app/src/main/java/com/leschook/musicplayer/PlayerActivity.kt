package com.leschook.musicplayer

import android.content.Intent
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.leschook.musicplayer.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {

    companion object {
        lateinit var musicList : ArrayList<Music>
        var musicPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var flag: Boolean = false
    }

    private lateinit var runnable: Runnable
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        musicPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicList = ArrayList()
                musicList.addAll(MainActivity.MusicList)
                setMusic()
                binding.playPauseBtn.setOnClickListener {
                    if (flag) {
                        pauseMusic()
                    } else {
                        playMusic()
                    }
                }
                binding.previousBtn.setOnClickListener {
                    previousOrNextMusic(false)
                }
                binding.nextBtn.setOnClickListener {
                    previousOrNextMusic(true)
                }
                binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if(p2) {
                            mediaPlayer!!.seekTo(p1)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) = Unit
                    override fun onStopTrackingTouch(p0: SeekBar?) = Unit
                })
                binding.equalizerBtn.setOnClickListener {
                    val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                    intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer!!.audioSessionId)
                    intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, baseContext.packageName)
                    intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                    startActivityForResult(intent, 13)
                }
            }
        }
    }

    private fun playMusic() {
        binding.playPauseBtn.setImageResource(R.drawable.ic_pause)
        flag = true
        mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.playPauseBtn.setImageResource(R.drawable.ic_play)
        flag = false
        mediaPlayer!!.pause()
    }

    private fun previousOrNextMusic(flag: Boolean) {
        if (flag) {
            if (musicList.size - 1 == musicPosition) {
                musicPosition = 0
            } else {
                ++musicPosition
            }
            setMusic()
        } else {
            if (musicPosition == 0) {
                musicPosition = musicList.size - 1
            } else {
                --musicPosition
            }
            setMusic()
        }
    }

    private fun setMusic() {
        Glide.with(this)
            .load(musicList[musicPosition].albumArtUri)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.musicImage)
        binding.musicName.text = musicList[musicPosition].title
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(musicList[musicPosition].path)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        flag = true
        binding.playPauseBtn.setImageResource(R.drawable.ic_pause)
        binding.seekBarEnd.text = duration(mediaPlayer!!.duration.toLong())
        binding.seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            binding.seekBarStatus.text = duration(mediaPlayer!!.currentPosition.toLong())
            binding.seekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}