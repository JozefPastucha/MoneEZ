package com.myoxidae.moneez

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.model.*


class TransactionListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AccountRepository = AccountRepository(application)

    var account: Account? = null
    var transaction: Transaction? = null
    var transactionWithCategory: TransactionWithCategoryData? = null

    fun getAccount(accountId: Long): LiveData<Account> {
        return repository.getAccount(accountId)
    }

    fun getTransactions(accountId: Long): LiveData<List<TransactionWithCategoryData>> {
        return repository.getTransactionsByAccount(accountId)
    }

    fun getTransaction(transactionId: Long): LiveData<TransactionWithCategoryData> {
        return repository.getTransaction(transactionId)
    }

    fun updateAccount(account: Account) {
        repository.update(account)
    }

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) {
        repository.updateTransaction(transaction)
    }

    fun deleteTransaction() {
        repository.deleteTransaction(transaction!!)
    }

    fun deleteAccount() {
        repository.delete(account!!)
    }

    fun insertTransactionPlan(transaction: Transaction) {
        repository.insertTransactionPlan(transaction)
    }
}