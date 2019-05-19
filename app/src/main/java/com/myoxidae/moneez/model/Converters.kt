package com.myoxidae.moneez.model

import androidx.room.TypeConverter
import java.util.*


class Converters {
    companion object {

        @TypeConverter
        @JvmStatic
        fun toAccountType(value: String): AccountType? {
            return when (value) {
                "Regular" -> AccountType.Regular
                "Cash" -> AccountType.Cash
                "Savings" -> AccountType.Savings
                "" -> null
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun toString(value: AccountType?): String {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toTransactionType(value: String): TransactionType? {
            return when (value) {
                "Income" -> TransactionType.Income
                "Spending" -> TransactionType.Spending
                "Withdrawal" -> TransactionType.Withdrawal
                "Transfer" -> TransactionType.Transfer
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun toTransactionType(value: TransactionType?): String {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toRepeatType(value: String): RepeatType? {
            return when (value) {
                "None" -> RepeatType.None
                "Daily" -> RepeatType.Daily
                "Weekly" -> RepeatType.Weekly
                "Monthly" -> RepeatType.Monthly
                "Yearly" -> RepeatType.Yearly
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun toRepeatType(value: RepeatType?): String {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toString(value: CategoryStatus?): String {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toCategoryStatus(value: String): CategoryStatus? {
            return when (value) {
                "Visible" -> CategoryStatus.Visible
                "Removed" -> CategoryStatus.Removed
                "Immutable" -> CategoryStatus.Immutable
                else -> null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toDate(value: Long?): Date? {
            return if (value == null) null else Date(value)
        }

        @JvmStatic
        @TypeConverter
        fun toLong(value: Date?): Long? {
            return value?.time
        }
    }
}