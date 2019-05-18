package com.myoxidae.moneez.picker

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

interface IconPickerListener {
    fun onSelectIcon(icon: MaterialDrawableBuilder.IconValue)
}