package com.github.dhaval2404.colorpicker

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.github.dhaval2404.colorpicker.adapter.RecentColorAdapter
import com.github.dhaval2404.colorpicker.databinding.DialogColorPickerBinding
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.listener.DismissListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.github.dhaval2404.colorpicker.util.SharedPref
import com.github.dhaval2404.colorpicker.util.setButtonTextColor
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class ColorPickerDialogFragment() : DialogFragment() {

    constructor(dialog: ColorPickerDialog) : this() {
        arguments = Bundle().apply {
            putString(EXTRA_TITLE, dialog.title)
            putString(EXTRA_POSITIVE_BUTTON, dialog.positiveButton)
            putString(EXTRA_NEGATIVE_BUTTON, dialog.negativeButton)

            putString(EXTRA_DEFAULT_COLOR, dialog.defaultColor)
            putParcelable(EXTRA_COLOR_SHAPE, dialog.colorShape)
        }

        this.colorListener = dialog.colorListener
        this.dismissListener = dialog.dismissListener
    }

    private var _binding: DialogColorPickerBinding? = null
    private val binding: DialogColorPickerBinding get() = _binding!!

    private var title: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null
    private var colorListener: ColorListener? = null
    private var dismissListener: DismissListener? = null
    private var defaultColor: String? = null
    private var colorShape: ColorShape = ColorShape.CIRCLE
    private lateinit var sharedPref: SharedPref
    private var positiveButtonClicked: Boolean = false

    companion object {

        private const val EXTRA_TITLE = "extra.title"
        private const val EXTRA_POSITIVE_BUTTON = "extra.positive_Button"
        private const val EXTRA_NEGATIVE_BUTTON = "extra.negative_button"

        private const val EXTRA_DEFAULT_COLOR = "extra.default_color"
        private const val EXTRA_COLOR_SHAPE = "extra.color_shape"

        fun newInstance(dialog: ColorPickerDialog): ColorPickerDialogFragment {
            return ColorPickerDialogFragment(dialog)
        }

    }

    fun setColorListener(listener: ColorListener?): ColorPickerDialogFragment {
        this.colorListener = listener
        return this
    }

    fun setDismissListener(listener: DismissListener?): ColorPickerDialogFragment {
        this.dismissListener = listener
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            title = it.getString(EXTRA_TITLE)
            positiveButton = it.getString(EXTRA_POSITIVE_BUTTON)
            negativeButton = it.getString(EXTRA_NEGATIVE_BUTTON)

            defaultColor = it.getString(EXTRA_DEFAULT_COLOR)
            colorShape = BundleCompat.getParcelable(it, EXTRA_COLOR_SHAPE, ColorShape::class.java)!!
        }
        val context = requireContext()

        // Create Dialog Instance
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setNegativeButton(negativeButton, null)

        // Setup Custom View
        _binding = DialogColorPickerBinding.inflate(layoutInflater)
        dialog.setView(binding.root)

        val colorPicker = binding.colorPicker
        val colorView = binding.colorView
        val recentColorsRV = binding.recentColorsRV

        val initialColor = if (!defaultColor.isNullOrBlank()) {
            Color.parseColor(defaultColor)
        } else {
            ContextCompat.getColor(context, R.color.grey_500)
        }
        colorView.setCardBackgroundColor(initialColor)

        colorPicker.setColor(initialColor)
        colorPicker.setColorListener { color, colorHex ->
            onColorSelected(color, colorHex)
        }

        sharedPref = SharedPref(context)
        val colorList = sharedPref.getRecentColors()

        // Setup Color Listing Adapter
        val adapter = RecentColorAdapter(colorList)
        adapter.setColorShape(colorShape)
        adapter.setColorListener { color, colorHex ->
            onRecentColorSelected(color, colorHex)
        }

        recentColorsRV.layoutManager = FlexboxLayoutManager(context)
        recentColorsRV.adapter = adapter

        // Set Submit Click Listener
        dialog.setPositiveButton(positiveButton) { _, _ ->
            positiveButtonClicked = true
        }

        return dialog.create().apply { setButtonTextColor() }
    }

    protected open fun onColorSelected(color: Int, colorHex: String) {
        binding.colorView.setCardBackgroundColor(color)
    }

    protected open fun onRecentColorSelected(color: Int, colorHex: String) {
        binding.colorPicker.setColor(color)
        binding.colorView.setCardBackgroundColor(color)
    }

    protected open fun callColorListener(color: Int, colorHex: String) {
        colorListener?.onColorSelected(color, colorHex)
        sharedPref.addColor(color = colorHex)
    }

    protected open fun callDismissListener() {
        dismissListener?.onDismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callDismissListener()
        if (positiveButtonClicked) {
            val color = binding.colorPicker.getColor()
            val colorHex = ColorUtil.formatColor(color)
            callColorListener(color, colorHex)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        callDismissListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}