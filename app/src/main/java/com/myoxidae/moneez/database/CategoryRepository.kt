package com.myoxidae.moneez.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.Transaction


class CategoryRepository(application: Application) {
    private val categoryDao: CategoryDao? //tieto otazniky jebnute som musel vsade najebat a constructory innerclass dat public
    val allCategories: LiveData<List<Category>>?

    init {
        val database = Database.getDatabase(application)
        categoryDao = database?.categoryDao()
        allCategories = categoryDao?.allCategories
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
}