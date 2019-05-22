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

    fun getAccount(accountId: Long): LiveData<Account> {
        return repository.getAccount(accountId)
    }

    fun getTransactions(accountId: Long): LiveData<List<TransactionWithCategoryData>> {
        return repository.getTransactionsByAccount(accountId)
    }

    fun updateAccount(account: Account) {
        repository.update(account)
    }

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
        //setAccount(transaction.accountId)
    }

    fun deleteAccount() {
        repository.delete(account!!)
    }

    fun insertTransactionPlan(transaction: Transaction) {
        repository.insertTransactionPlan(transaction)
    }
}