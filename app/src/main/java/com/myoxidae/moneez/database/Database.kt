package com.myoxidae.moneez.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.Converters

@androidx.room.Database(entities = [com.myoxidae.moneez.model.Transaction::class, com.myoxidae.moneez.model.TransactionPlan::class, Account::class, Category::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)

abstract class Database : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private var database: Database? = null

        fun getDatabase(appContext: Context): Database? {
            if (database == null) {
                database = Room.databaseBuilder(appContext, Database::class.java, "MoneEZ-DB").build()
            }
            return database
        }
    }
}