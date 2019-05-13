package com.myoxidae.moneez.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

//TODO cascade delete - for. keys
@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    val accountId: Long,
    val type: TransactionType,
    val name: String,
    val amount: Double,
    val description: String,
    val date: Date,
    val categoryId: Long,
    val repeat: RepeatType,
    val recipient: String
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var transactionId: Long = 0
}