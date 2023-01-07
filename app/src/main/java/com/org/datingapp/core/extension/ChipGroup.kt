package com.org.datingapp.core.extension

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.addChip(label : String, context : Context, @ColorRes strokeColor : Int, strokeWidthInDp : Float) {
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isClickable = true
        isCheckable = false
        isCheckedIconVisible = false
        chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, strokeColor))
        chipStrokeWidth = strokeWidthInDp
        addView(this)
    }
}