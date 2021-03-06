package com.myoxidae.moneez.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "accounts")
@Parcelize
data class Account(
    val type: AccountType,
    val name: String,
    val description: String,
    val initialBalance: Double,
    var currentBalance: Double,
    val interest: Double,
    val currency: String,
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var accountId: Long = 0
): Parcelable