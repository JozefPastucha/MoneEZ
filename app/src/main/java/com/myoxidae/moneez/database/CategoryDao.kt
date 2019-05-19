package com.myoxidae.moneez.database

import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myoxidae.moneez.model.Category

@Dao
interface CategoryDao {
    @get:Query("SELECT * FROM categories")
    val allCategories: LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE categoryId == :categoryId LIMIT 1")
    fun getCategory(categoryId: Long): Category

    @Query("SELECT * FROM categories")
    fun allCategoriesList(): List<Category>


    @Query("SELECT * FROM categories WHERE categoryId == :categoryId LIMIT 1")
    fun getCategoryLiveData(categoryId: Long): LiveData<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(item: Category): Long

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}
