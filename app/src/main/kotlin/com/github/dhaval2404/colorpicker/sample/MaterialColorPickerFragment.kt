package com.github.dhaval2404.colorpicker.sample

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.listener.DismissListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.github.dhaval2404.colorpicker.reinstateFragmentListenersIfApplicable
import com.github.dhaval2404.colorpicker.sample.databinding.FragmentMaterialColorPickerBinding
import com.github.dhaval2404.colorpicker.util.ColorUtil

/**
 * MaterialColorPicker Demo
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 26 Dec 2019
 */
class MaterialColorPickerFragment : Fragment() {

    private var mMaterialColorSquare: String = ""
    private var mMaterialColorCircle = ""
    private var mMaterialColorBottomSheet = ""
    private var mMaterialPreDefinedColor = ""

    private var _binding: FragmentMaterialColorPickerBinding? = null
    private val binding: FragmentMaterialColorPickerBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val materialDialogFragmentPickerSquareBtnColorListener =
            ColorListener { color, colorHex ->
                mMaterialColorSquare = colorHex
                setButtonBackground(binding.materialDialogFragmentPickerSquareBtn, color)
            }

        binding.materialDialogFragmentPickerSquareBtn.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireActivity()) // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE) // Or ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300) // Default ColorSwatch._500
                .setDefaultColor(mMaterialColorSquare) // Pass Default Color
                .setColorListener(materialDialogFragmentPickerSquareBtnColorListener)
                .build()
                .showDialog(childFragmentManager, "materialDialogFragmentPickerSquareBtn")
        }

        childFragmentManager.reinstateFragmentListenersIfApplicable(
            tag = "materialDialogFragmentPickerSquareBtn",
            colorListener = materialDialogFragmentPickerSquareBtnColorListener,
            dismissListener = null
        )

        val materialDialogFragmentPickerCircleBtnColorListener =
            ColorListener { color, colorHex ->
                mMaterialColorCircle = colorHex
                setButtonBackground(binding.materialDialogFragmentPickerCircleBtn, color)
            }

        val materialDialogFragmentPickerCircleBtnDismissListener = DismissListener { Log.d("MaterialDialogPicker", "Handle dismiss event") }

        binding.materialDialogFragmentPickerCircleBtn.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireActivity())
                .setColorSwatch(ColorSwatch._500)
                .setDefaultColor(mMaterialColorCircle)
                .setColorListener(materialDialogFragmentPickerCircleBtnColorListener)
                .setDismissListener(materialDialogFragmentPickerCircleBtnDismissListener)
                .build()
                .showDialog(childFragmentManager, "materialDialogFragmentPickerCircleBtn")
        }

        childFragmentManager.reinstateFragmentListenersIfApplicable(
            tag = "materialDialogFragmentPickerCircleBtn",
            colorListener = materialDialogFragmentPickerCircleBtnColorListener,
            dismissListener = materialDialogFragmentPickerCircleBtnDismissListener
        )

        binding.materialDialogPickerSquareBtn.setOnClickListener { _ ->
            MaterialColorPickerDialog
                .Builder(requireActivity()) // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE) // Or ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300) // Default ColorSwatch._500
                .setDefaultColor(mMaterialColorSquare) // Pass Default Color
                .setColorListener { color, colorHex ->
                    mMaterialColorSquare = colorHex
                    setButtonBackground(binding.materialDialogPickerSquareBtn, color)
                }
                .show()
        }

        binding.materialDialogPickerCircleBtn.setOnClickListener { _ ->
            MaterialColorPickerDialog
                .Builder(requireActivity())
                .setColorSwatch(ColorSwatch._500)
                .setDefaultColor(mMaterialColorCircle)
                .setColorListener { color, colorHex ->
                    mMaterialColorCircle = colorHex
                    setButtonBackground(binding.materialDialogPickerCircleBtn, color)
                }
                .setDismissListener {
                    Log.d("MaterialDialogPicker", "Handle dismiss event")
                }
                .show()
        }

        val materialBottomSheetDialogBtnColorListener =
            ColorListener { color, colorHex ->
                mMaterialColorBottomSheet = colorHex
                setButtonBackground(binding.materialBottomSheetDialogBtn, color)
            }

        val materialBottomSheetDialogBtnDismissListener = DismissListener { Log.d("MaterialBottomSheet", "Handle dismiss event") }

        binding.materialBottomSheetDialogBtn.setOnClickListener { _ ->
            MaterialColorPickerDialog
                .Builder(requireActivity())
                .setColorSwatch(ColorSwatch._300)
                .setDefaultColor(mMaterialColorBottomSheet)
                .setColorListener(materialBottomSheetDialogBtnColorListener)
                .setDismissListener(materialBottomSheetDialogBtnDismissListener)
                .showBottomSheet(childFragmentManager, "materialBottomSheetDialogBtn")
        }

        childFragmentManager.reinstateFragmentListenersIfApplicable(
            tag = "materialBottomSheetDialogBtn",
            colorListener = materialBottomSheetDialogBtnColorListener,
            dismissListener = materialBottomSheetDialogBtnDismissListener
        )

        val materialPreDefinedColorPickerBtnColorListener =
            ColorListener { color, colorHex ->
                mMaterialPreDefinedColor = colorHex
                setButtonBackground(binding.materialPreDefinedColorPickerBtn, color)
            }

        binding.materialPreDefinedColorPickerBtn.setOnClickListener { _ ->
            MaterialColorPickerDialog
                .Builder(requireActivity())
                // .setColors(arrayListOf("#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb", "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"))
                // .setColors(resources.getStringArray(R.array.themeColorHex))
                .setColorRes(resources.getIntArray(R.array.themeColors))
                .setDefaultColor(mMaterialPreDefinedColor)
                .setColorListener(materialPreDefinedColorPickerBtnColorListener)
                .showBottomSheet(childFragmentManager, "materialPreDefinedColorPickerBtn")
        }

        childFragmentManager.reinstateFragmentListenersIfApplicable(
            tag = "materialPreDefinedColorPickerBtn",
            colorListener = materialPreDefinedColorPickerBtnColorListener,
            dismissListener = null
        )
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
