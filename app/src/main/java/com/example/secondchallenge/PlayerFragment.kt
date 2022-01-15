package com.example.secondchallenge

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.secondchallenge.databinding.PlayerFragmentBinding
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff

class PlayerFragment : Fragment() {

    private lateinit var binding: PlayerFragmentBinding
    private var paused: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private val detailsFragment = DetailsFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.player_fragment, container, false)
        setHasOptionsMenu(true)
        binding.prevButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        binding.playStopButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        binding.nextButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        binding.playStopButton.setOnClickListener { playStop() }
        activity?.applicationContext?.let {
            (activity as MainActivity).manager.createMediaPlayer(it)
            (activity as MainActivity).manager.associateInfo(it)
            val context = it
            binding.nextButton.setOnClickListener {
                nextSongWithDataUpdateandListener(context)
            }
            binding.prevButton.setOnClickListener {
                prevSongWithDataUpdate(context)
            }
            if (!(activity as MainActivity).manager.listenerWhenCompleted) {
                (activity as MainActivity).manager.song?.setOnCompletionListener {
                    nextSongWithDataUpdateandListener(context)
                }
            }
        }
        if ((activity as MainActivity).manager.song!!.isPlaying()) binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24) else
            binding.playStopButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        updateData()
        sharedPreferences = activity?.getSharedPreferences("general_settings", MODE_PRIVATE)!!
        return binding.root
    }

    fun prevSongWithDataUpdate(context: Context) {
        (activity as MainActivity).manager.prevSong(context)
        binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
        updateData()
    }

    fun nextSongWithDataUpdateandListener(context: Context) {
        println(detailsFragment.isVisible)
        if (detailsFragment.isVisible) detailsFragment.dismiss()
        (activity as MainActivity).manager.nextSong(context)
        binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
        (activity as MainActivity).manager.song?.setOnCompletionListener {
            nextSongWithDataUpdateandListener(context)
        }
        (activity as MainActivity).manager.listenerWhenCompleted = true
        updateData()
    }

    fun updateData() {
        binding.coverImageView.setImageBitmap((activity as MainActivity).manager.cover)
        binding.songNameData.text = (activity as MainActivity).manager.songName
        binding.artistData.text = (activity as MainActivity).manager.artist
    }

    // MENU CONFIGURATION - PART I
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.float_menu, menu)
    }

    // MENU CONFIGURATION - PART II
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        detailsFragment.show(activity?.supportFragmentManager!!, "Details")
        return true
    }

    private fun playStop() {
        if (!(activity as MainActivity).manager.song!!.isPlaying()) {
            binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
            val current = sharedPreferences.getInt("current", 0)
            (activity as MainActivity).manager.song!!.seekTo(current)
            (activity as MainActivity).manager.song!!.start()
        } else {
            (activity as MainActivity).manager.song!!.pause()
            binding.playStopButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            paused = true
            saveCurrent()
        }
    }

    override fun onPause() {
        if (!paused) {
            saveCurrent()
        }
        super.onPause()
    }

    fun saveCurrent() {
        val editor = sharedPreferences!!.edit()
        editor.putInt("current", (activity as MainActivity).manager.song!!.currentPosition)
        editor.putInt("currentSong", (activity as MainActivity).manager.currentSong!!)
        editor.commit()
    }
}