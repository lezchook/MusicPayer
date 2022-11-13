package com.leschook.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.leschook.musicplayer.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {

    companion object {
        lateinit var musicList : ArrayList<Music>
        var musicPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var flag: Boolean = false
    }

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
    }
}