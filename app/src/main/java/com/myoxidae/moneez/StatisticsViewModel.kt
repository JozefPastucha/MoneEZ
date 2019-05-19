package com.myoxidae.moneez

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.database.AccountRepository
import com.myoxidae.moneez.database.CategoryRepository
import com.myoxidae.moneez.database.Database
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.StatisticsCategory
import java.util.*

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CategoryRepository = CategoryRepository(application)
    private val allCategories: LiveData<List<Category>>?
    var accountId: Long = 1
    var monthYear: Date? = null
    init {
        var cal = Calendar.getInstance()
        cal.clear()
        cal.set(Calendar.YEAR, 2019)
        cal.set(Calendar.MONTH, 3)
        monthYear = cal.time
        allCategories = repository.allCategories
    }

    fun set(v:
            Map<Long?, List<StatisticsCategory>>) {
        var x  = v
    }
    fun getSumsForCategories(): LiveData<List<StatisticsCategory>>? {
        return repository.sumsForCategories(accountId)
    }
    fun getSumsForMonthAndCategories(): LiveData<List<StatisticsCategory>>? {
        return repository.transactionsWithCategoryName(accountId)
    }
}