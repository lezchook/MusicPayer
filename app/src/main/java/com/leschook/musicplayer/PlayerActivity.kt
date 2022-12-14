package com.leschook.musicplayer

import android.media.MediaPlayer
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
        var favourite: Boolean = false
        var fIndex: Int = -1
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
                binding.favouriteBtn.setOnClickListener {
                    if (favourite) {
                        favourite = false
                        binding.favouriteBtn.setImageResource(R.drawable.ic_favorite_border)
                        FavouriteActivity.favouriteSongs.removeAt(fIndex)
                    } else {
                        favourite = true
                        binding.favouriteBtn.setImageResource(R.drawable.ic_favorite)
                        FavouriteActivity.favouriteSongs.add(musicList[musicPosition])
                    }
                }
            }
            "FavouriteAdapter" -> {
                musicList = ArrayList()
                musicList.addAll(FavouriteActivity.favouriteSongs)
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
                binding.favouriteBtn.setOnClickListener {
                    if (favourite) {
                        favourite = false
                        binding.favouriteBtn.setImageResource(R.drawable.ic_favorite_border)
                        FavouriteActivity.favouriteSongs.removeAt(fIndex)
                    } else {
                        favourite = true
                        binding.favouriteBtn.setImageResource(R.drawable.ic_favorite)
                        FavouriteActivity.favouriteSongs.add(musicList[musicPosition])
                    }
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
        fIndex = favouriteCheck(musicList[musicPosition].id)
        Glide.with(this)
            .load(musicList[musicPosition].albumArtUri)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.musicImage)
        binding.musicName.text = musicList[musicPosition].title
        if (favourite) {
            binding.favouriteBtn.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.favouriteBtn.setImageResource(R.drawable.ic_favorite_border)
        }
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