package com.leschook.musicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leschook.musicplayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<String>): RecyclerView.Adapter<MusicAdapter.MyHolder>() {
    class MyHolder(binding: MusicViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.songName
        val album = binding.songAlbum
        val image = binding.imageMusic
        val length = binding.songLength
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position]
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}