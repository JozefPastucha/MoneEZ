package com.myoxidae.moneez.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

// Mate tady TransactionPlan, Transaction a TransactionWithCategoryData, ktere si navzajem castecne replikuji obsah
// Nebudu se pokouset to pochopit, protoze bych musel byt mnohem dukladnejsi, ale rekl bych, ze tady mate chybu v navzhu architektury a datoveho modelu
@Parcelize
data class TransactionWithCategoryData(
    val accountId: Long,
    val type: TransactionType,
    val name: String,
    var amount: Double,
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