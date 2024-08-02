package com.github.dhaval2404.colorpicker.sample

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.sample.databinding.FragmentColorPickerBinding
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.github.dhaval2404.colorpicker.util.SharedPref

/**
 * ColorPicker Demo
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 26 Dec 2019
 */
class ColorPickerFragment : Fragment() {

    private var mColor = 0
    private var _binding: FragmentColorPickerBinding? = null
    private val binding: FragmentColorPickerBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        mColor = SharedPref(requireContext()).getRecentColor(primaryColor)
        binding.colorPickerBtn.setOnClickListener { _ ->
            ColorPickerDialog
                .Builder(requireActivity()) // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE) // Or ColorShape.CIRCLE
                .setDefaultColor(mColor) // Pass Default Color
                .setColorListener { color, _ ->
                    mColor = color
                    binding.colorPickerView.setColor(color)
                    setButtonBackground(binding.colorPickerBtn, color)
                }
                .setDismissListener {
                    Log.d("ColorPickerDialog", "Handle dismiss event")
                }
                .show()
        }
    }

    private fun setButtonBackground(btn: AppCompatButton, color: Int) {
        if (ColorUtil.isDarkColor(color)) {
            btn.setTextColor(Color.WHITE)
        } else {
            btn.setTextColor(Color.BLACK)
        }
        btn.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
