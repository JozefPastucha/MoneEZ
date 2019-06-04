package com.myoxidae.moneez.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.model.Account
import android.provider.ContactsContract.CommonDataKinds.Note
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionPlan


class AccountListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AccountRepository = AccountRepository(application)
    private val allAccounts: LiveData<List<Account>>?

    init {
        allAccounts = repository.allAccounts
    }

    fun insert(account: Account) {
        repository.insert(account)
    }

    fun update(account: Account) {
        repository.update(account)
    }

    fun delete(account: Account) {
        repository.delete(account)
    }

    fun getAllAccounts(): LiveData<List<Account>>? {
        return allAccounts
    }

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }

    fun insertTransactionPlan(transactionPlan: Transaction) {
        repository.insertTransactionPlan(transactionPlan)
    }
  
    fun getAccountsList(): List<Account> {
        return repository.accountsList()
    }

}