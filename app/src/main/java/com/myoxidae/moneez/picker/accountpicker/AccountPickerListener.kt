package com.myoxidae.moneez.picker.accountpicker

import com.myoxidae.moneez.model.Account

interface AccountPickerListener {
    fun onSelectAccount(account: Account)
}