package com.myoxidae.moneez.database

import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myoxidae.moneez.model.*

@Dao
interface AccountDao {
    @get:Query("SELECT * FROM accounts")
    val allAccounts: LiveData<List<Account>>

    @Query("SELECT * FROM accounts WHERE accountId == :accountId LIMIT 1")
    fun getAccount(accountId: Long): Account

    @Query("SELECT * FROM accounts WHERE accountId == :accountId LIMIT 1")
    fun getAccountLiveData(accountId: Long): LiveData<Account>

    /*@Query("SELECT * FROM transactions WHERE accountId == :accountId")
    fun accountTransactions(accountId: Long): LiveData<List<Transaction>>*/

    @Query("SELECT * FROM (SELECT * FROM transactions WHERE accountId == :accountId) NATURAL JOIN (SELECT categoryId, name AS cName, icon AS cIcon, color AS cColor FROM categories)")
    fun accountTransactions(accountId: Long): LiveData<List<TransactionWithCategoryData>>

    @Query("SELECT * FROM transactionPlans")
    fun transactionPlans(): List<TransactionPlan>

    @Query("SELECT * FROM accounts")
    fun allAccountsList(): List<Account>

    @Query("SELECT * FROM transactions WHERE transactionId == :transactionId LIMIT 1")
    fun getTransaction(transactionId: Long): Transaction

    @Query("SELECT * FROM (SELECT * FROM transactions WHERE transactionId == :transactionId) NATURAL JOIN (SELECT categoryId, name AS cName, icon AS cIcon, color AS cColor FROM categories)")
    fun getTransactionLiveData(transactionId: Long): LiveData<TransactionWithCategoryData>

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

    @androidx.room.Transaction
    fun deleteTransactionUpdateAccount(transaction: Transaction) {
        val account = getAccount(transaction.accountId)
        if (transaction.type == TransactionType.Income) {
            account.currentBalance -= transaction.amount
        } else if (transaction.type == TransactionType.Spending) {
            account.currentBalance += transaction.amount
        }
        deleteTransaction(transaction)
        updateAccount(account)
    }

    @androidx.room.Transaction
    fun updateTransactionUpdateAccount(transaction: Transaction) {
        val account = getAccount(transaction.accountId)
        val oldTransaction = getTransaction(transaction.transactionId)
        val oldValue = oldTransaction.amount
        if (transaction.type == TransactionType.Income) {
            account.currentBalance -= oldValue
            account.currentBalance += transaction.amount
        } else if (transaction.type == TransactionType.Spending) {
            account.currentBalance += oldValue
            account.currentBalance -= transaction.amount
        }
        updateTransaction(transaction)
        updateAccount(account)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransactionPlan(transactionPlan: TransactionPlan): Long


    @Update
    fun updateAccount(account: Account)

    @Update
    fun updateTransaction(transaction: Transaction)

    @Update
    fun updateTransactionPlan(transactionPlan: TransactionPlan)

    @Delete
    fun deleteAccount(account: Account)

    @androidx.room.Transaction
    fun deleteAccountCascade(account: Account) {
        deleteTransactions(account.accountId)
        deleteTransactionPlans(account.accountId)
        deleteAccount(account)
    }

    @Query("DELETE FROM transactions WHERE accountId == :accountId")
    fun deleteTransactions(accountId: Long)

    @Delete
    fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactionPlans WHERE accountId == :accountId")
    fun deleteTransactionPlans(accountId: Long)
}
