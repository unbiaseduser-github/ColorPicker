@file:JvmName("FragmentListenerPatcher")

package com.github.dhaval2404.colorpicker

import androidx.fragment.app.FragmentManager
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.listener.DismissListener

fun FragmentManager.reinstateFragmentListenersIfApplicable(
    tag: String,
    colorListener: ColorListener?,
    dismissListener: DismissListener?
) {
    val fragment = findFragmentByTag(tag) ?: return
    when (fragment) {
        is MaterialColorPickerDialogFragment -> {
            fragment.setColorListener(colorListener).setDismissListener(dismissListener)
        }
        is MaterialColorPickerBottomSheet -> {
            fragment.setColorListener(colorListener).setDismissListener(dismissListener)
        }
        is ColorPickerDialogFragment -> {
            fragment.setColorListener(colorListener).setDismissListener(dismissListener)
        }
    }
}