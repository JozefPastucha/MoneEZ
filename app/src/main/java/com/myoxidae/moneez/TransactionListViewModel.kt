package com.myoxidae.moneez

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Transaction


class TransactionListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AccountRepository = AccountRepository(application)
    //var accountId: Long = 0

    fun getAccount(accountId: Long): LiveData<Account> {
        return repository.getAccount(accountId)
    }

    fun getTransactions(accountId: Long): LiveData<List<Transaction>> {
        return repository.getTransactionsByAccount(accountId)
    }

    fun updateAccount(account: Account) {
        repository.update(account)
    }

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }

}