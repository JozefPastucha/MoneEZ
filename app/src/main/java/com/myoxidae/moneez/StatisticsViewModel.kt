package com.myoxidae.moneez

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.database.CategoryRepository
import com.myoxidae.moneez.database.Database
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.StatisticsCategory
import com.myoxidae.moneez.model.TransactionWithCategoryData
import java.util.*

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AccountRepository = AccountRepository(application)
    //private val allCategories: LiveData<List<Category>>?
    var accountId: Long = 0
    var monthYear: Date? = null

    fun transactionsWithCategoryName(): LiveData<List<TransactionWithCategoryData>>? {
        return repository.getTransactionsByAccount(accountId)
    }
}