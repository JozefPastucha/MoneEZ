package com.pv239_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pv239_project.database.AccountRepository
import com.pv239_project.model.Account
import android.provider.ContactsContract.CommonDataKinds.Note
import com.pv239_project.model.Transaction


class AccountListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AccountRepository = AccountRepository(application)
    private val allAccounts: LiveData<List<Account>>? //dalsi jebnuty ?

    init {
        allAccounts = repository.allAccounts
    }

    fun insert(account: Account) {
        repository.insert(account)
    }

    fun update(account: Account) {
        repository.update(account)
    }

    fun delete(account: Account) {//a transakcie by sme mohli pridat
        repository.delete(account)
    }

    fun getAllAccounts(): LiveData<List<Account>>? { //dalsi drbnuty otaznik
        return allAccounts
    }

    fun insertTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }

    /*fun deleteAllAccounts() {
        repository.deleteAllAccounts()
    }*/
}