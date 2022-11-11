package com.leschook.musicplayer

data class Music(val id: String, val title: String, val album: String, val artist: String, val length: Long = 0, val path: String)