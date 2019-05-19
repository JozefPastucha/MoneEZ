package com.myoxidae.moneez.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Transaction


class AccountRepository(application: Application) {
    private val accountDao: AccountDao? //tieto otazniky jebnute som musel vsade najebat a constructory innerclass dat public
    val allAccounts: LiveData<List<Account>>?

    init {
        val database = Database.getDatabase(application)
        accountDao = database?.accountDao()
        allAccounts = accountDao?.allAccounts
    }

    fun insert(account: Account) {
        InsertAccountAsyncTask(accountDao).execute(account)
    }

    fun update(account: Account) {
        UpdateAccountAsyncTask(accountDao).execute(account)
    }

    fun delete(account: Account) {
        DeleteAccountAsyncTask(accountDao).execute(account)
    }

    fun insertTransaction(transaction: Transaction) {
        InsertTransactionAsyncTask(accountDao).execute(transaction)
    }

    fun getAccount(accountId: Long): LiveData<Account> {
        return GetAccountAsyncTask(accountDao, accountId).execute().get()

    }
    fun getTransactionsByAccount(accountId: Long): LiveData<List<Transaction>> {
        return GetTransactionsByAccountAsyncTask(accountDao, accountId).execute().get()
    }

    private class InsertAccountAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Account, Transaction, Void>() {

        override fun doInBackground(vararg accounts: Account): Void? {
            accountDao?.addAccount(accounts[0])
            return null
        }
    }

    private class UpdateAccountAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Account, Void, Void>() {

        override fun doInBackground(vararg accounts: Account): Void? {
            accountDao?.updateAccount(accounts[0])
            return null
        }
    }

    private class DeleteAccountAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Account, Void, Void>() {

        override fun doInBackground(vararg accounts: Account): Void? {
            accountDao?.deleteAccountCascade(accounts[0])
            return null
        }
    }

    private class InsertTransactionAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Transaction, Void, Void>() {

        override fun doInBackground(vararg transactions: Transaction): Void? {
            accountDao?.addTransactionUpdateAccount(transactions[0])
            return null
        }
    }

    private class GetTransactionsByAccountAsyncTask(private val accountDao: AccountDao?, private val accountId: Long) :
        AsyncTask<Account, Void, LiveData<List<Transaction>>>() {
        override fun doInBackground(vararg accounts: Account): LiveData<List<Transaction>>? {
            return accountDao?.accountTransactions(accountId)
        }
    }

    private class GetAccountAsyncTask(private val accountDao: AccountDao?, private val accountId: Long) :
        AsyncTask<Account, Void, LiveData<Account>>() {

        override fun doInBackground(vararg accounts: Account): LiveData<Account>? {
            return accountDao?.getAccountLiveData(accountId)
            //return null
        }
    }
}