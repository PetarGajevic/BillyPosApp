package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.view.model.Category
import io.reactivex.Maybe

@Dao
interface ItemDao {

    @Insert
    fun insert(item: LocalItem): Maybe<Long>

    @Update
    fun update(item: LocalItem): Maybe<Int>

    @Query("SELECT * FROM item")
    fun getItems(): Maybe<List<LocalItem>>

    @Query("SELECT * FROM item WHERE itemName LIKE '%' || :input || '%'")
    fun searchItems(input: String?): Maybe<List<LocalItem>>

    @Query("SELECT * FROM item WHERE id = :id")
    fun findItemById(id: Long): Maybe<LocalItem>

    @Query("SELECT * FROM subcategory WHERE id_category = :categoryId")
    fun getSubCategoriesByCatId(categoryId: Long?): Maybe<List<LocalSubCategory>>

    @Query("SELECT * FROM category")
    fun getCategories(): Maybe<List<LocalCategory>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getItemCategory(id: Long?): Maybe<LocalCategory>

    @Query("SELECT * FROM subcategory WHERE id = :id")
    fun getItemSubCategory(id: Long?): Maybe<LocalSubCategory>

    @Query("SELECT categoryName FROM category WHERE id = :idCategory")
    fun getCategoryByItemId(idCategory: Long?): Maybe<String>

    @Query("SELECT subcategoryName FROM subcategory WHERE id = :idSubCategory")
    fun getSubCategoryByItemId(idSubCategory: Long?): Maybe<String>
}