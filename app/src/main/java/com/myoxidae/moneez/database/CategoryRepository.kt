package com.myoxidae.moneez.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.StatisticsCategory
import com.myoxidae.moneez.model.Transaction
import java.util.*


class CategoryRepository(application: Application) {
    private val categoryDao: CategoryDao? //tieto otazniky jebnute som musel vsade najebat a constructory innerclass dat public
    val allCategories: LiveData<List<Category>>?

    init {
        val database = Database.getDatabase(application)
        categoryDao = database?.categoryDao()
        allCategories = categoryDao?.allCategories
    }

    fun sumsForCategories(accountId: Long): LiveData<List<StatisticsCategory>> {
        return SumsCategories(categoryDao, accountId).execute().get()
    }

    fun transactionsWithCategoryName(accountId: Long): LiveData<List<StatisticsCategory>> {
        return TransactionsWithCategoryName(categoryDao, accountId).execute().get()
    }

    fun categoriesList(): List<Category> {
        return GetCategoriesAsyncTask(categoryDao).execute().get()

    }

    fun insert(category: Category) {
        InsertCategoryAsyncTask(categoryDao).execute(category)
    }

    fun update(category: Category) {
        UpdateCategoryAsyncTask(categoryDao).execute(category)
    }

    fun delete(category: Category) {
        DeleteCategoryAsyncTask(categoryDao).execute(category)
    }

    fun getCategory(categoryId: Long): LiveData<Category> {
        return GetCategoryAsyncTask(categoryDao, categoryId).execute().get()
    }

    private class InsertCategoryAsyncTask (private val categoryDao: CategoryDao?) :
        AsyncTask<Category, Transaction, Void>() {

        override fun doInBackground(vararg categories: Category): Void? {
            categoryDao?.addCategory(categories[0])
            return null
        }
    }

    private class UpdateCategoryAsyncTask (private val categoryDao: CategoryDao?) :
        AsyncTask<Category, Void, Void>() {

        override fun doInBackground(vararg categories: Category): Void? {
            categoryDao?.updateCategory(categories[0])
            return null
        }
    }

    private class DeleteCategoryAsyncTask (private val categoryDao: CategoryDao?) :
        AsyncTask<Category, Void, Void>() {

        override fun doInBackground(vararg categories: Category): Void? {
            categoryDao?.deleteCategory(categories[0])
            return null
        }
    }

    private class GetCategoryAsyncTask(private val categoryDao: CategoryDao?, private val categoryId: Long) :
        AsyncTask<Category, Void, LiveData<Category>>() {

        override fun doInBackground(vararg categories: Category): LiveData<Category>? {
            return categoryDao?.getCategoryLiveData(categoryId)
            //return null
        }
    }

    private class SumsCategories(private val categoryDao: CategoryDao?, private val accountId: Long) :
        AsyncTask<Category, Void, LiveData<List<StatisticsCategory>>>() {

        override fun doInBackground(vararg categories: Category): LiveData<List<StatisticsCategory>>? {
            return categoryDao?.sumsForCategories(accountId)
            //return null
        }
    }

    private class TransactionsWithCategoryName(private val categoryDao: CategoryDao?, private val accountId: Long) :
        AsyncTask<Category, Void, LiveData<List<StatisticsCategory>>>() {

        override fun doInBackground(vararg categories: Category): LiveData<List<StatisticsCategory>>? {
            return categoryDao?.transactionsWithCategoryName(accountId)
            //return null
        }
     }

     private class GetCategoriesAsyncTask(private val categoryDao: CategoryDao?) :
        AsyncTask<Category, Void, List<Category>>() {
        override fun doInBackground(vararg categories: Category): List<Category>? {
            return categoryDao?.allCategoriesList()

        }
    }
}
