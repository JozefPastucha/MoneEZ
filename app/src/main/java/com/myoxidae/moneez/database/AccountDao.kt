package com.myoxidae.moneez.database

import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionType

@Dao
interface AccountDao {
    @get:Query("SELECT * FROM accounts")
    val allAccounts: LiveData<List<Account>>

    @Query("SELECT * FROM accounts WHERE accountId == :accountId LIMIT 1")
    fun getAccount(accountId: Long): Account

    @Query("SELECT * FROM accounts WHERE accountId == :accountId LIMIT 1")
    fun getAccountLiveData(accountId: Long): LiveData<Account>

    @Query("SELECT * FROM transactions WHERE accountId == :accountId")
    fun accountTransactions(accountId: Long): LiveData<List<Transaction>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(item: Account): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction): Long

    @androidx.room.Transaction
    fun addTransactionUpdateAccount(transaction: Transaction): Long {
        val id = addTransaction(transaction)
        val account = getAccount(transaction.accountId)
        if (transaction.type == TransactionType.Income) {
            account.currentBalance += transaction.amount
        } else if (transaction.type == TransactionType.Spending) {
            account.currentBalance -= transaction.amount
        }
        updateAccount(account)
        return id
    }

    @Update
    fun updateAccount(account: Account)

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteAccount(account: Account)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}
