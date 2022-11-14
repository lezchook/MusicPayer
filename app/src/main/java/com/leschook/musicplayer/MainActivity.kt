package com.leschook.musicplayer

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.leschook.musicplayer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicAdapter: MusicAdapter

    companion object {
        lateinit var MusicList: ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
        initializeLayout()
        binding.favouriteBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
        }
        binding.playlist.setOnClickListener {

        }
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
            }
        }
    }

    private fun initializeLayout() {
        MusicList = getAllMusicFiles()
        binding.musicRecyclerView.setHasFixedSize(true)
        binding.musicRecyclerView.setItemViewCacheSize(13)
        binding.musicRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, MusicList)
        binding.musicRecyclerView.adapter = musicAdapter
    }

    @SuppressLint("Recycle", "Range")
    private fun getAllMusicFiles(): ArrayList<Music> {
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ALBUM_ID)
        val select = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
        MediaStore.Audio.Media.DATE_ADDED + " DESC", null)
        if (select != null) {
            if (select.moveToFirst()) {
                do {
                    val id = select.getString(select.getColumnIndex(MediaStore.Audio.Media._ID))
                    val title = select.getString(select.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val album = select.getString(select.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist = select.getString(select.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val length = select.getLong(select.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val path = select.getString(select.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val idColumn = select.getColumnIndex(MediaStore.Audio.Media._ID)
                    val thisId = select.getLong(idColumn);
                    val albumArtUri = ContentUris.withAppendedId(uri, thisId);
                    val music = Music(id, title, album, artist, length, path, albumArtUri)
                    val file =  File(music.path)
                    if (file.exists()) {
                        tempList.add(music)
                    }
                } while (select.moveToNext())
                select.close()
            }
        }
        return tempList
    }
}