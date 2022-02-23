package com.wind.billypos.view.ui.category.repository

import com.wind.billypos.data.dao.CategoryDao
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalOperator
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.data.remote.api.CategoryApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.category.RemoteCategoryCreateResponse
import com.wind.billypos.data.remote.model.category.RemoteCategoryUpdateResponse
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val categoryApi: CategoryApi
) {

    private val billyPosMapper = BillyPosMapper()

    fun insertCategory(category: LocalCategory): Maybe<LocalCategory> =
        categoryDao.insert(category = category).flatMap {
            findCategoryById(id = it)
        }

    fun sendCategory(category: LocalCategory): Maybe<RemoteCategoryCreateResponse> =
        categoryApi.sendCategory(category = billyPosMapper.categoryMapper.toRemoteModel(category))


    fun sendUpdateCategory(category: LocalCategory): Maybe<RemoteCategoryUpdateResponse> =
        categoryApi.updateCategory(
            category = billyPosMapper.categoryMapper.toRemoteModel(category),
            id = category.id
        )


    fun update(category: LocalCategory): Maybe<Int> = categoryDao.update(category = category)

    fun updateSubCategories(id: Long?): Single<Boolean> =
        Single.create {
            categoryDao.updateSubCategories(id = id)
            it.onSuccess(true)
        }

    fun updateCategory(category: LocalCategory): Maybe<LocalCategory> =
        categoryDao.update(category = category).flatMap {
            findCategoryById(id = category.id!!)
        }


    fun updateSubCategory(subcategory: LocalSubCategory): Maybe<LocalSubCategory> =
        categoryDao.update(subcategory = subcategory).flatMap {
            findSubCategoryById(id = subcategory.id!!)
        }

    fun getCategories(): Maybe<List<LocalCategory>> = categoryDao.getCategories()

    fun searchCategories(input: String?): Maybe<List<LocalCategory>> =
        categoryDao.searchCategories(input = input)

    fun findCategoryById(id: Long?): Maybe<LocalCategory> =
        categoryDao.findCategoryById(id = id)

    private fun findSubCategoryById(id: Long): Maybe<LocalSubCategory> =
        categoryDao.findSubCategoryById(id = id)

    fun getSubcategories(id: Long?): Maybe<List<LocalSubCategory>> =
        categoryDao.getSubcategories(id = id)

    fun searchSubCategories(input: String?, idCategory: Long?): Maybe<List<LocalSubCategory>> =
        categoryDao.searchSubCategories(input = input, idCategory = idCategory)

    fun updateSubCategories(id: Long?, isActive: Boolean) =
        Single.create<Boolean> {
            categoryDao.updateSubCategories(id = id, isActive = isActive)
            it.onSuccess(true)
        }

    fun activateCategory(id: Long?) = Single.create<Boolean> {
        categoryDao.activateCategory(id = id)
        it.onSuccess(true)
    }


}