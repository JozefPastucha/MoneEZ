package com.myoxidae.moneez

import android.app.Application
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.model.RepeatType
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionPlan
import java.text.SimpleDateFormat
import java.util.*

class AddPastTransactionsAndNewPlan() {
    companion object {
        fun addPastTransactionsAndNewPlan(newTransaction: Transaction, application: Application) {
            val repository = AccountRepository(application)
            val s = SimpleDateFormat("DD/MM/YYYY")
            val cal = Calendar.getInstance()
            val thisTime = cal.time
            cal.time = newTransaction.date
            val thisDate = s.format(cal.time)

            //if the transaction was in the past
            if (newTransaction.repeat == RepeatType.Daily) {
                cal.add(Calendar.DAY_OF_YEAR, 1)
                //if it is in the past -> add it
                while (cal.time < thisTime) {
                    //clone, because objects saved to database would have the same date
                    val newTransaction = newTransaction.copy()
                    newTransaction.date = cal.time
                    repository.insertTransaction(newTransaction)
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                }
                //if it it now/in the future -> add it only if it is today
                if (s.format(cal.time) == thisDate) {
                    repository.insertTransaction(newTransaction)
                }

            } else if (newTransaction.repeat == RepeatType.Weekly) {
                cal.add(Calendar.DAY_OF_YEAR, 7)
                //if it is in the past -> add it
                while (cal.time < thisTime) {
                    //clone, because objects saved to database would have the same date
                    val newTransaction = newTransaction.copy()
                    newTransaction.date = cal.time
                    repository.insertTransaction(newTransaction)
                    cal.add(Calendar.DAY_OF_YEAR, 7)
                }
                //if it it now/in the future -> add it only if it is today
                if (s.format(cal.time) == thisDate) {
                    repository.insertTransaction(newTransaction)
                }
            } else if (newTransaction.repeat == RepeatType.Monthly) {
                cal.add(Calendar.MONTH, 1)
                //if it is in the past -> add it
                while (cal.time < thisTime) {
                    //clone, because objects saved to database would have the same date
                    val newTransaction = newTransaction.copy()
                    newTransaction.date = cal.time
                    repository.insertTransaction(newTransaction)
                    cal.add(Calendar.MONTH, 1)
                }
                //if it it now/in the future, add it only if it is today
                if (s.format(cal.time) == thisDate) {
                    repository.insertTransaction(newTransaction)
                }
            } else if (newTransaction.repeat == RepeatType.Yearly) {
                cal.add(Calendar.YEAR, 1)
                while (cal.time < thisTime) {
                    //clone, because objects saved to database would have the same date
                    val newTransaction = newTransaction.copy()
                    newTransaction.date = cal.time
                    repository.insertTransaction(newTransaction)
                    cal.add(Calendar.YEAR, 1)
                }
                //if it it now/in the future -> add it only if it is today
                if (s.format(cal.time) == thisDate)
                    cal.add(Calendar.YEAR, 1)
                if (cal.time >= thisTime) {
                    repository.insertTransaction(newTransaction)
                }
            }
            repository.insertTransactionPlan(newTransaction)
        }
    }
}