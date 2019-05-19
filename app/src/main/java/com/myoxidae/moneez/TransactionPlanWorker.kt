package com.myoxidae.moneez

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.myoxidae.moneez.activity.MainActivity
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.model.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.DAYS

import java.util.concurrent.TimeUnit.SECONDS

internal class TransactionPlanWorker(application: Application, private val context: LifecycleOwner) {
    private val scheduler = Executors.newScheduledThreadPool(1)
    private val repository: AccountRepository = AccountRepository(application)
    fun start() {
        val worker = Runnable {
            val plans = repository.getTransactionPlans()
            val s = SimpleDateFormat("DD/MM/YYYY")
            val cal = Calendar.getInstance()
            val thisTime = cal.time
            val thisDate = s.format(cal.time)
            plans.forEach {
                //set calendar for each plan
                cal.time = it.lastTime
                if (it.repeat == RepeatType.Daily) {
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                    //if it is in the past -> add it
                    while(cal.time < thisTime) {
                        insertTransaction(it, cal.time)
                        cal.add(Calendar.DAY_OF_YEAR, 1)
                    }
                    //if it it now/in the future -> add it only if it is today
                    if(s.format(cal.time) == s.format(thisDate)) {
                        insertTransaction(it, cal.time)
                    }

                } else if (it.repeat == RepeatType.Weekly) {
                    cal.add(Calendar.DAY_OF_YEAR, 7)
                    //if it is in the past -> add it
                    while(cal.time < thisTime) {
                        insertTransaction(it, cal.time)
                        cal.add(Calendar.DAY_OF_YEAR, 7)
                    }
                    //if it it now/in the future -> add it only if it is today
                    if(s.format(cal.time) == s.format(thisDate)) {
                        insertTransaction(it, cal.time)
                    }
                } else if (it.repeat == RepeatType.Monthly) {
                    cal.add(Calendar.MONTH, 1)
                    //if it is in the past -> add it
                    while(cal.time < thisTime) {
                        insertTransaction(it, cal.time)
                        cal.add(Calendar.MONTH, 1)
                    }
                    //if it it now/in the future, add it only if it is today
                    if(s.format(cal.time) == s.format(thisDate)) {
                        insertTransaction(it, cal.time)
                    }
                } else if (it.repeat == RepeatType.Yearly) {
                    cal.add(Calendar.YEAR, 1)
                    while(cal.time < thisTime) {
                        insertTransaction(it, cal.time)
                        cal.add(Calendar.YEAR, 1)
                    }
                    //if it it now/in the future -> add it only if it is today
                    if(s.format(cal.time) == s.format(thisDate))
                        cal.add(Calendar.YEAR, 1)
                    if(cal.time >= thisTime) {
                        insertTransaction(it, cal.time)
                    }
                }
            }
        }
            val dispatcherHandle = scheduler.scheduleAtFixedRate(worker, 10, 1, DAYS)
        scheduler.schedule({ dispatcherHandle.cancel(true) }, (60 * 60).toLong(), SECONDS)
    }

    private fun insertTransaction(it: TransactionPlan, date: Date) {
        //should do this using db transactions
        var cal = Calendar.getInstance()
        repository.insertTransaction(
            Transaction(
                it.accountId,
                it.type,
                it.name,
                it.amount,
                it.description,
                date,
                it.categoryId,
                it.repeat,
                it.recipient
            )
        )
        it.lastTime = cal.time
        repository.updateTransactionPlan(
            it
        )
    }
}