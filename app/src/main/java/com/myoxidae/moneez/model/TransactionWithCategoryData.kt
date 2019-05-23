package com.myoxidae.moneez.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TransactionWithCategoryData(
    val accountId: Long,
    val type: TransactionType,
    val name: String,
    val amount: Double,
    val description: String,
    val date: Date,
    val repeat: RepeatType,
    val recipient: String,
    var transactionId : Long,

    val categoryId: Long,
    val cName: String,
    val cIcon: String,
    var cColor: String
): Parcelable {
}