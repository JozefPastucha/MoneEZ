package com.myoxidae.moneez.model

import androidx.room.TypeConverter
import java.util.*


class Converters {
    companion object {

        @TypeConverter
        @JvmStatic
        fun toAccountType(value: String) : AccountType?
        {
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
        fun toString(value: AccountType?) : String
        {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toTransactionType(value: String) : TransactionType?
        {
            return when (value) {
                "Regular" -> TransactionType.Cash
                "Cash" -> TransactionType.CreditCard
                "" -> null
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun toString(value: Category?) : String
        {
            return value?.toString() ?: ""
        }

        @TypeConverter
        @JvmStatic
        fun toCategory(value: String) : Category?
        {
            return when (value) {
                "Home" -> Category.Home
                "Food" -> Category.Food
                "Health" -> Category.Health

                "" -> null
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun toTransactionType(value: TransactionType?) : String
        {
            return value?.toString() ?: ""
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