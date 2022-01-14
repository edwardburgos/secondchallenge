package com.example.secondchallenge

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.secondchallenge.databinding.DetailsFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailsFragment : BottomSheetDialogFragment() {

    lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)
        binding.apply {
            var interpretes: List<String> = Manager.artist.split(", ")
            for (e in interpretes) {
                var interpreter: TextView = TextView(activity?.applicationContext)
                interpreter.text = e
                interpreter.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.normal_letter)
                )
                interpreter.setTextColor(Color.BLACK)
                interpretedByData.addView(
                    interpreter
                )
            }
            albumData.text = Manager.album
            generoData.text = Manager.genero
        }
        return binding.root
    }
}