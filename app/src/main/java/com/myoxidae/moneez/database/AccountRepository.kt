package com.myoxidae.moneez.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionPlan


class AccountRepository(application: Application) {
    private val accountDao: AccountDao? //tieto otazniky jebnute som musel vsade najebat a constructory innerclass dat public
    val allAccounts: LiveData<List<Account>>?

    init {
        val database = Database.getDatabase(application)
        accountDao = database?.accountDao()
        allAccounts = accountDao?.allAccounts
    }

    fun accountsList(): List<Account> {
        return GetAccountsAsyncTask(accountDao).execute().get()
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

    fun updateTransaction(transaction: Transaction) {
        UpdateTransactionAsyncTask(accountDao).execute(transaction)
    }

    fun deleteTransaction(transaction: Transaction) {
        DeleteTransactionAsyncTask(accountDao).execute(transaction)
    }

    fun insertTransaction(transaction: Transaction) {
        InsertTransactionAsyncTask(accountDao).execute(transaction)
    }

    fun insertTransactionPlan(transaction: Transaction) {
        var plan = TransactionPlan(transaction.accountId, transaction.type, transaction.name, transaction.amount,
            transaction.description, transaction.date, transaction.categoryId, transaction.repeat,
            transaction.recipient, transaction.date
        )
        plan.lastTime = transaction.date
        InsertTransactionPlanAsyncTask(accountDao).execute(plan)
    }

    fun updateTransactionPlan(transactionPlan: TransactionPlan) {
        UpdateTransactionPlanAsyncTask(accountDao).execute(transactionPlan)
    }

    fun getAccount(accountId: Long): LiveData<Account> {
        return GetAccountAsyncTask(accountDao, accountId).execute().get()

    }
    fun getTransactionsByAccount(accountId: Long): LiveData<List<Transaction>> {
        return GetTransactionsByAccountAsyncTask(accountDao, accountId).execute().get()
    }
    fun getTransaction(transactionId: Long): LiveData<Transaction> {
        return GetTransactionAsyncTask(accountDao, transactionId).execute().get()
    }

    fun getTransactionPlans(): List<TransactionPlan> {
        return GetTransactionPlansAsyncTask(accountDao).execute().get()
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

    private class UpdateTransactionAsyncTask(private val accountDao: AccountDao?) :
        AsyncTask<Transaction, Void, Void>() {

        override fun doInBackground(vararg transactions: Transaction): Void? {
            accountDao?.updateTransactionUpdateAccount(transactions[0])
            return null
        }
    }

    private class DeleteTransactionAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<Transaction, Void, Void>() {

        override fun doInBackground(vararg transactions: Transaction): Void? {
            accountDao?.deleteTransactionUpdateAccount(transactions[0])
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

    private class InsertTransactionPlanAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<TransactionPlan, Void, Void>() {

        override fun doInBackground(vararg transactionPlans: TransactionPlan): Void? {
            accountDao?.addTransactionPlan(transactionPlans[0])
            return null
        }
    }

    private class UpdateTransactionPlanAsyncTask (private val accountDao: AccountDao?) :
        AsyncTask<TransactionPlan, Void, Void>() {

        override fun doInBackground(vararg transactionPlans: TransactionPlan): Void? {
            accountDao?.updateTransactionPlan(transactionPlans[0])
            return null
        }
    }

    private class GetTransactionsByAccountAsyncTask(private val accountDao: AccountDao?, private val accountId: Long) :
        AsyncTask<Account, Void, LiveData<List<Transaction>>>() {
        override fun doInBackground(vararg accounts: Account): LiveData<List<Transaction>>? {
            return accountDao?.accountTransactions(accountId)
        }
    }

    private class GetTransactionAsyncTask(private val accountDao: AccountDao?, private val transactionId: Long) :
        AsyncTask<Transaction, Void, LiveData<Transaction>>() {

        override fun doInBackground(vararg transactions: Transaction): LiveData<Transaction>? {
            return accountDao?.getTransactionLiveData(transactionId)
        }
    }

    private class     GetTransactionPlansAsyncTask(private val accountDao: AccountDao?) :
        AsyncTask<Account, Void, List<TransactionPlan>>() {
        override fun doInBackground(vararg accounts: Account): List<TransactionPlan>? {
            return accountDao?.transactionPlans()
        }
    }

    private class GetAccountAsyncTask(private val accountDao: AccountDao?, private val accountId: Long) :
        AsyncTask<Account, Void, LiveData<Account>>() {

        override fun doInBackground(vararg accounts: Account): LiveData<Account>? {
            return accountDao?.getAccountLiveData(accountId)
            //return null
        }
    }

    private class GetAccountsAsyncTask(private val accountDao: AccountDao?) :
        AsyncTask<Account, Void, List<Account>>() {

        override fun doInBackground(vararg accounts: Account): List<Account>? {
            return accountDao?.allAccountsList()
        }
    }
}