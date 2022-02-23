package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalOperator
import com.wind.billypos.data.local.model.LocalSubCategory
import io.reactivex.Maybe

@Dao
interface CategoryDao {

    @Insert
    fun insert(category: LocalCategory): Maybe<Long>

    @Update
    fun update(category: LocalCategory): Maybe<Int>

    @Insert
    fun insert(subcategory: LocalSubCategory): Maybe<Long>

    @Update
    fun update(subcategory: LocalSubCategory): Maybe<Int>

    @Query("UPDATE subcategory SET isActive = 0 WHERE id_category = :id")
    fun updateSubCategories(id: Long?)

    @Query("UPDATE subcategory SET isActive = :isActive WHERE id_category = :id")
    fun updateSubCategories(id: Long?, isActive: Boolean)

    @Query("SELECT * FROM category WHERE id = :id")
    fun findCategoryById(id: Long?): Maybe<LocalCategory>

    @Query("SELECT * FROM subcategory WHERE id = :id")
    fun findSubCategoryById(id: Long): Maybe<LocalSubCategory>

    @Query("SELECT * FROM category WHERE isActive = 1")
    fun getCategories(): Maybe<List<LocalCategory>>

    @Query("SELECT * FROM category WHERE categoryName LIKE '%' || :input || '%'")
    fun searchCategories(input: String?): Maybe<List<LocalCategory>>

    @Query("SELECT * FROM subcategory WHERE id_category = :id")
    fun getSubcategories(id: Long?): Maybe<List<LocalSubCategory>>

    @Query("SELECT * FROM subcategory WHERE subcategoryName LIKE '%' || :input || '%' AND id_category = :idCategory")
    fun searchSubCategories(input: String?, idCategory: Long?): Maybe<List<LocalSubCategory>>

    @Query("UPDATE category SET isActive = 1 WHERE id = :id")
    fun activateCategory(id: Long?)

}