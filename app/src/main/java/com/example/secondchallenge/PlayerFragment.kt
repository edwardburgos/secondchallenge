package com.example.secondchallenge

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.secondchallenge.databinding.PlayerFragmentBinding
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import com.example.secondchallenge.Manager.Companion

class PlayerFragment : Fragment() {

    lateinit var binding: PlayerFragmentBinding
    var paused: Boolean = false
    lateinit var sharedPreferences: SharedPreferences

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
            Companion.createMediaPlayer(it)
            Companion.associateInfo(it)
            var context = it
            binding.nextButton.setOnClickListener{
                Companion.nextSong(context)
                binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
                updateData()
            }
        }
        if (Companion.song!!.isPlaying()) binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24) else
            binding.playStopButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        updateData()
        sharedPreferences = activity?.getSharedPreferences("general_settings", MODE_PRIVATE)!!
        return binding.root
    }

    fun updateData() {
        binding.coverImageView.setImageBitmap(Companion.cover)
        binding.songNameData.text = Companion.songName
        binding.artistData.text = Companion.artist
    }

    // MENU CONFIGURATION - PART I
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.float_menu, menu)
    }

    // MENU CONFIGURATION - PART II
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val detailsFragment = DetailsFragment()
        detailsFragment.show(activity?.supportFragmentManager!!, "Details")
        return true
    }

    private fun playStop() {
        if (!Manager.song!!.isPlaying()) {
            binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
            val current = sharedPreferences.getInt("current", 0)
            Manager.song!!.seekTo(if (current == 164201) 0 else current)
            Manager.song!!.start()
        } else {
            Manager.song!!.pause()
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
        editor.putInt("current", Manager.song!!.currentPosition)
        editor.commit()
    }
}