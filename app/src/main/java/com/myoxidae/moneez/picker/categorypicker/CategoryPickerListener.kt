package com.myoxidae.moneez.picker.categorypicker

import com.myoxidae.moneez.model.Category

interface CategoryPickerListener {
    fun onSelectCategory(category: Category)
}