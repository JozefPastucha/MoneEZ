package com.myoxidae.moneez.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "transactionPlans")
@Parcelize
data class TransactionPlan(
    val accountId: Long,
    val type: TransactionType,
    val name: String,
    val amount: Double,
    val description: String,
    val date: Date,
    val categoryId: Long,
    val repeat: RepeatType,
    val recipient: String,
    var lastTime: Date?
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var transactionPlanId: Long = 0
}