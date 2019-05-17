package com.myoxidae.moneez.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.Converters
import androidx.sqlite.db.SupportSQLiteDatabase


@androidx.room.Database(
    entities = [com.myoxidae.moneez.model.Transaction::class, Account::class, Category::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)

abstract class Database : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private var database: Database? = null

        fun getDatabase(appContext: Context): Database? {
            if (database == null) {
                val databaseCallback = object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        TODO idk
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        // do something every time database is open
                    }
                }

                database = Room.databaseBuilder(appContext, Database::class.java, "MoneEZ-DB")
                    .addCallback(databaseCallback)
                    .build()
            }
            return database
        }
    }
}