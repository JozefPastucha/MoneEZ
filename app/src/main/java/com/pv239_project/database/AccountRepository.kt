package com.pv239_project.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.pv239_project.model.Account
import com.pv239_project.model.Transaction


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

    /*fun deleteAllAccounts() {
        DeleteAllAccountsAsyncTask(accountDao).execute()
    }*/

    private class InsertAccountAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Account, Void, Void>() {

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
            accountDao?.deleteAccount(accounts[0])
            return null
        }
    }

    private class InsertTransactionAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Transaction, Void, Void>() {

        override fun doInBackground(vararg transactions: Transaction): Void? {
            accountDao?.addTransaction(transactions[0])
            return null
        }
    }

    /*private class DeleteAllAccountsAsyncTask private constructor(private val accountDao: AccountDao?) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            accountDao.deleteAllAccounts()
            return null
        }
    }*/
}