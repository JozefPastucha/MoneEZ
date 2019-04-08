package com.pv239_project.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.pv239_project.model.Account
import com.pv239_project.model.Converters

@androidx.room.Database(entities = [com.pv239_project.model.Transaction::class, Account::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)

abstract class Database : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    companion object {
        private var database: Database? = null

        fun getDatabase(appContext: Context): Database? {
            if (database == null) {
                database = Room.databaseBuilder(appContext, Database::class.java, "PV239-project-DB").build()
            }
            return database
        }
    }
}