package com.myoxidae.moneez.picker.iconpicker

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

interface IconPickerListener {
    fun onSelectIcon(icon: MaterialDrawableBuilder.IconValue)
}