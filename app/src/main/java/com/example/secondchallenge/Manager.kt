package com.example.secondchallenge

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.reflect.Field


class Manager : Application() {
    companion object {

        var song: MediaPlayer? = null
        var mmr: MediaMetadataRetriever? = null
        lateinit var cover: Bitmap
        var songName = "unknown"
        var artist = "unknown"
        var album = "unknown"
        var genero = "unknown"
        var currentSong: Int? = null
        var contador = 0
        lateinit var canciones: List<Int>

        fun createMediaPlayer(con: Context) {
            canciones = listOf(
                R.raw.tainylosientobb,
                R.raw.badgyalrema44,
                R.raw.debilidad,
                R.raw.entrenosotrosremix,
                R.raw.harakakikolosbobosonmio,
                R.raw.inmortales,
                R.raw.remix911,
                R.raw.tiagobiza,
                R.raw.truenodancecrip,
                R.raw.verteir
            )
            currentSong = canciones[contador]
            if (song == null) {
                song = MediaPlayer.create(con!!, currentSong!!)
            }
        }

        fun nextSong(con: Context) {
            if (contador != 9) contador += 1 else contador = 0
            currentSong = canciones[contador]
            song?.stop()
            song?.reset()
            song?.release()
            song = MediaPlayer.create(con!!, currentSong!!)
            associateInfo(con!!)
            song?.start()
        }

        fun associateInfo(con: Context) {
            if (mmr == null) mmr = MediaMetadataRetriever()
            mmr!!.setDataSource(
                con,
                Uri.parse("android.resource://com.example.secondchallenge/${currentSong!!}")
            )
            cover = createCover(mmr!!, con)
            songName = mmr!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            artist = mmr!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
            album = mmr!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
            var genreCode =
                mmr!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE).toString()
                    .substring(1, 3)

            // READ RAW FILE
            var reader: List<String> =
                BufferedReader(InputStreamReader(con.resources.openRawResource(R.raw.genres))).readLines();
            for (eleme in reader) {
                if (eleme.substring(0, 2) == genreCode) {
                    genero = eleme.substring(3)
                    break
                }
            }
        }

        private fun createCover(mmr: MediaMetadataRetriever, con: Context): Bitmap {
            val embedPic = mmr.embeddedPicture
            var bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic!!.size)
            return bitmap ?: BitmapFactory.decodeResource(
                con.resources,
                R.drawable.ic_baseline_music_note_24
            )
        }
    }
}