package com.myoxidae.moneez

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myoxidae.moneez.database.CategoryRepository
import com.myoxidae.moneez.model.Category
import android.provider.ContactsContract.CommonDataKinds.Note
import com.myoxidae.moneez.model.Transaction


class CategoryListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CategoryRepository = CategoryRepository(application)
    private val allCategories: LiveData<List<Category>>?

    init {
        allCategories = repository.allCategories
    }

    fun insert(category: Category) {
        repository.insert(category)
    }

    fun update(category: Category) {
        repository.update(category)
    }

    fun delete(category: Category) {
        repository.delete(category)
    }

    fun getAllCategories(): LiveData<List<Category>>? {
        return allCategories
    }

    fun getCategoriesList(): List<Category> {
        return repository.categoriesList()
    }
}