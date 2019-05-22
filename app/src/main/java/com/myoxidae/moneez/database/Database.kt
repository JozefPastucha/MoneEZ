package com.myoxidae.moneez.database

import android.content.Context
import android.telecom.Call
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myoxidae.moneez.model.*

@androidx.room.Database(
    entities = [com.myoxidae.moneez.model.Transaction::class, Account::class, Category::class, TransactionPlan::class],
    version = 6,
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

                database = Room.databaseBuilder(appContext, Database::class.java, "MoneEZ-DB")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            it does not work idk...
                            super.onCreate(db)
                            db.execSQL("INSERT INTO categories (categoryId,name,description,icon,color,status) VALUES (0, 'Transfer','Transfer between accounts', 'BANK', '000000', 'Immutable');")
                            db.execSQL("INSERT INTO categories (categoryId,name,description,icon,color,status) VALUES (1, 'Default','When you don select any category', 'SHAPE', '000000', 'Immutable');")
                            db.execSQL("INSERT INTO categories (categoryId,name,description,icon,color,status) VALUES (2, 'Food','Food', 'FOOD_FORK_DRINK', 'f40202', 'Visible');")
                            db.execSQL("INSERT INTO categories (categoryId,name,description,icon,color,status) VALUES (3, 'Sport','Sport', 'BASKETBALL', '02f416', 'Visible');")
                            db.execSQL("INSERT INTO categories (categoryId,name,description,icon,color,status) VALUES (4, 'Transportation','Transportation', 'TRAIN', '0232f4', 'Visible');")
                        }
                    })
                    .build()
            }
            return database
        }
    }
}