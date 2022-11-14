package com.leschook.musicplayer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.leschook.musicplayer.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var musicAdapter: FavouriteAdapter

    companion object{
        var favouriteSongs: ArrayList<Music> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.favourite.setHasFixedSize(true)
        binding.favourite.setItemViewCacheSize(13)
        binding.favourite.layoutManager = LinearLayoutManager(this@FavouriteActivity)
        musicAdapter = FavouriteAdapter(this@FavouriteActivity, favouriteSongs)
        binding.favourite.adapter = musicAdapter
    }
}