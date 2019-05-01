package com.pv239_project.database

import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pv239_project.model.Account
import com.pv239_project.model.Transaction

@Dao
interface AccountDao {
    @get:Query("SELECT * FROM accounts")
    val allAccounts: LiveData<List<Account>>


    @Query("SELECT * FROM transactions WHERE accountId == :accountId")
    fun accountTransactions(accountId: Long): LiveData<List<Transaction>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(item: Account): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction): Long

    @Update
    fun updateAccount(account: Account)

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteAccount(account: Account)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}
