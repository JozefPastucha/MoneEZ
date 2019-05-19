package com.myoxidae.moneez.database

import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.StatisticsCategory
import java.util.*

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

    /*@Query("SELECT sum, name FROM ((SELECT categoryId, SUM(amount) FROM transaction WHERE accountId == :accountId GROUP BY categoryId) s NATURAL JOIN category))")
    fun howToName1(accountId: Long)
*/

    @Query("SELECT amount, name FROM ((SELECT categoryId, SUM(amount) AS amount FROM transactions WHERE accountId == :accountId GROUP BY categoryId) AS s)  JOIN categories on s.categoryId == categories.categoryId")
    fun sumsForCategories(accountId: Long) : LiveData<List<StatisticsCategory>>

    //@Query("SELECT year_month, name, sum FROM ((SELECT to_char(date,'YY-Mon') as year_month, categoryId, SUM(amount) AS sum FROM transactions t WHERE accountId = 1 GROUP BY to_char(date,'YY-Mon'), categoryId) AS s) JOIN categories on s.categoryId == categories.categoryId")
    //@Query("SELECT year_month, name, sum FROM ((SELECT strftime('%m', datetime(date)) as year_month, categoryId, SUM(amount) AS sum FROM transactions t WHERE accountId = 1 GROUP BY to_char(date,'YY-Mon'), categoryId) AS s) JOIN categories on s.categoryId == categories.categoryId")
    //@Query("SELECT year_month, name, sum FROM ((SELECT strftime('%m', Date(date)) as year_month, categoryId, SUM(amount) AS sum FROM transactions t WHERE accountId == :accountId  GROUP BY strftime('%m', Date(date)), categoryId) AS s) JOIN categories on s.categoryId == categories.categoryId")

    //@Query("SELECT year_month, name, sum FROM ((SELECT date as year_month, categoryId, SUM(amount) AS sum FROM transactions t WHERE accountId == :accountId  GROUP BY date, categoryId) AS s) JOIN categories on s.categoryId == categories.categoryId")

    @Query("SELECT t.date, c.categoryId, c.name, t.type, t.amount  FROM (SELECT * from transactions WHERE accountId == :accountId) t JOIN categories c on t.categoryId == c.categoryId")
    fun transactionsWithCategoryName(accountId: Long) : LiveData<List<StatisticsCategory>>

}
