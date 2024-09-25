package com.github.dhaval2404.colorpicker

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.dhaval2404.colorpicker.adapter.MaterialColorPickerAdapter
import com.github.dhaval2404.colorpicker.databinding.DialogBottomsheetMaterialColorPickerBinding
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.listener.DismissListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Color Picker from Predefined color set in BottomSheetDialogFragment
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 24 Dec 2019
 */
open class MaterialColorPickerBottomSheet() : BottomSheetDialogFragment() {

    constructor(dialog: MaterialColorPickerDialog) : this() {
        arguments = Bundle().apply {
            putString(EXTRA_TITLE, dialog.title)
            putString(EXTRA_POSITIVE_BUTTON, dialog.positiveButton)
            putString(EXTRA_NEGATIVE_BUTTON, dialog.negativeButton)

            putString(EXTRA_DEFAULT_COLOR, dialog.defaultColor)
            putParcelable(EXTRA_COLOR_SWATCH, dialog.colorSwatch)
            putParcelable(EXTRA_COLOR_SHAPE, dialog.colorShape)
            putBoolean(EXTRA_IS_TICK_COLOR_PER_CARD, dialog.isTickColorPerCard)

            var list: ArrayList<String>? = null
            if (dialog.colors != null) {
                list = ArrayList(dialog.colors)
            }
            putStringArrayList(EXTRA_COLORS, list)
        }

        this.colorListener = dialog.colorListener
        this.dismissListener = dialog.dismissListener
    }

    private var title: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null
    private var colorListener: ColorListener? = null
    private var dismissListener: DismissListener? = null
    private var defaultColor: String? = null
    private var colorShape: ColorShape = ColorShape.CIRCLE
    private var colorSwatch: ColorSwatch = ColorSwatch._300
    private var colors: List<String>? = null
    private var isTickColorPerCard: Boolean = false
    private lateinit var adapter: MaterialColorPickerAdapter
    private var positiveButtonClicked: Boolean = false

    private var _binding: DialogBottomsheetMaterialColorPickerBinding? = null
    private val binding: DialogBottomsheetMaterialColorPickerBinding get() = _binding!!

    companion object {

        private const val EXTRA_TITLE = "extra.title"
        private const val EXTRA_POSITIVE_BUTTON = "extra.positive_Button"
        private const val EXTRA_NEGATIVE_BUTTON = "extra.negative_button"

        private const val EXTRA_DEFAULT_COLOR = "extra.default_color"
        private const val EXTRA_COLOR_SHAPE = "extra.color_shape"
        private const val EXTRA_COLOR_SWATCH = "extra.color_swatch"
        private const val EXTRA_COLORS = "extra.colors"
        private const val EXTRA_IS_TICK_COLOR_PER_CARD = "extra.is_tick_color_per_card"

        fun getInstance(dialog: MaterialColorPickerDialog): MaterialColorPickerBottomSheet {
            return MaterialColorPickerBottomSheet(dialog)
        }
    }

    fun setColorListener(listener: ColorListener?): MaterialColorPickerBottomSheet {
        this.colorListener = listener
        return this
    }

    fun setDismissListener(listener: DismissListener?): MaterialColorPickerBottomSheet {
        this.dismissListener = listener
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBottomsheetMaterialColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            title = it.getString(EXTRA_TITLE)
            positiveButton = it.getString(EXTRA_POSITIVE_BUTTON)
            negativeButton = it.getString(EXTRA_NEGATIVE_BUTTON)

            defaultColor = it.getString(EXTRA_DEFAULT_COLOR)
            colorSwatch = it.getParcelable(EXTRA_COLOR_SWATCH)!!
            colorShape = it.getParcelable(EXTRA_COLOR_SHAPE)!!

            colors = it.getStringArrayList(EXTRA_COLORS)
            isTickColorPerCard = it.getBoolean(EXTRA_IS_TICK_COLOR_PER_CARD)
        }

        title?.let { binding.titleTxt.text = it }
        positiveButton?.let { binding.positiveBtn.text = it }
        negativeButton?.let { binding.negativeBtn.text = it }

        val colorList = colors ?: ColorUtil.getColors(requireContext(), colorSwatch.value)
        adapter = MaterialColorPickerAdapter(colorList)
        adapter.setColorShape(colorShape)
        adapter.setTickColorPerCard(isTickColorPerCard)
        if (!defaultColor.isNullOrBlank()) {
            adapter.setDefaultColor(defaultColor!!)
        }

        binding.materialColorRV.setHasFixedSize(true)
        binding.materialColorRV.layoutManager = FlexboxLayoutManager(context)
        binding.materialColorRV.adapter = adapter

        binding.positiveBtn.setOnClickListener {
            positiveButtonClicked = true
            dismiss()
        }
        binding.negativeBtn.setOnClickListener { dismiss() }
    }

    protected open fun onColorListenerCalled(color: Int, colorHex: String) {
        colorListener?.onColorSelected(color, colorHex)
    }

    protected open fun onDismissListenerCalled() {
        dismissListener?.onDismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListenerCalled()
        if (positiveButtonClicked) {
            val color = adapter.getSelectedColor()
            if (color.isNotBlank()) {
                onColorListenerCalled(ColorUtil.parseColor(color), color)
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDismissListenerCalled()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
