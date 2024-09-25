package com.github.dhaval2404.colorpicker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.github.dhaval2404.colorpicker.adapter.MaterialColorPickerAdapter
import com.github.dhaval2404.colorpicker.databinding.DialogMaterialColorPickerBinding
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.listener.DismissListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.github.dhaval2404.colorpicker.util.setButtonTextColor
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class MaterialColorPickerDialogFragment() : DialogFragment() {

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

    private var _binding: DialogMaterialColorPickerBinding? = null
    private val binding: DialogMaterialColorPickerBinding get() = _binding!!

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

    companion object {

        private const val EXTRA_TITLE = "extra.title"
        private const val EXTRA_POSITIVE_BUTTON = "extra.positive_Button"
        private const val EXTRA_NEGATIVE_BUTTON = "extra.negative_button"

        private const val EXTRA_DEFAULT_COLOR = "extra.default_color"
        private const val EXTRA_COLOR_SHAPE = "extra.color_shape"
        private const val EXTRA_COLOR_SWATCH = "extra.color_swatch"
        private const val EXTRA_COLORS = "extra.colors"
        private const val EXTRA_IS_TICK_COLOR_PER_CARD = "extra.is_tick_color_per_card"

        fun newInstance(dialog: MaterialColorPickerDialog): MaterialColorPickerDialogFragment {
            return MaterialColorPickerDialogFragment(dialog)
        }
    }

    fun setColorListener(listener: ColorListener?): MaterialColorPickerDialogFragment {
        this.colorListener = listener
        return this
    }

    fun setDismissListener(listener: DismissListener?): MaterialColorPickerDialogFragment {
        this.dismissListener = listener
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            title = it.getString(EXTRA_TITLE)
            positiveButton = it.getString(EXTRA_POSITIVE_BUTTON)
            negativeButton = it.getString(EXTRA_NEGATIVE_BUTTON)

            defaultColor = it.getString(EXTRA_DEFAULT_COLOR)
            colorSwatch = BundleCompat.getParcelable(it, EXTRA_COLOR_SWATCH, ColorSwatch::class.java)!!
            colorShape = BundleCompat.getParcelable(it, EXTRA_COLOR_SHAPE, ColorShape::class.java)!!

            colors = it.getStringArrayList(EXTRA_COLORS)
            isTickColorPerCard = it.getBoolean(EXTRA_IS_TICK_COLOR_PER_CARD)
        }
        val context = requireContext()
        // Create Dialog Instance
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setNegativeButton(negativeButton, null)

        // Create Custom View for Dialog
        _binding = DialogMaterialColorPickerBinding.inflate(layoutInflater)
        dialog.setView(binding.root)

        // Setup Color Listing Adapter
        val colorList = colors ?: ColorUtil.getColors(context, colorSwatch.value)
        adapter = MaterialColorPickerAdapter(colorList) { color ->
            onColorSelected(ColorUtil.parseColor(color), color)
        }
        adapter.setColorShape(colorShape)
        adapter.setTickColorPerCard(isTickColorPerCard)
        if (!defaultColor.isNullOrBlank()) {
            adapter.setDefaultColor(defaultColor!!)
        }

        // Setup Color RecyclerView
        val materialColorRV = binding.materialColorRV
        materialColorRV.setHasFixedSize(true)
        materialColorRV.layoutManager = FlexboxLayoutManager(context)
        materialColorRV.adapter = adapter

        // Set Submit Click Listener
        dialog.setPositiveButton(positiveButton) { _, _ ->
            positiveButtonClicked = true
        }

        return dialog.create().apply { setButtonTextColor() }
    }

    protected open fun onColorSelected(color: Int, colorHex: String) = Unit

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