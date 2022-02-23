package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.google.android.material.circularreveal.CircularRevealHelper
import com.wind.billypos.data.local.model.LocalCategory
import com.wind.billypos.data.local.model.LocalConfiguration
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalSubCategory
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: LocalCategory): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubCategory(subCategory: LocalSubCategory): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: LocalItem): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfiguration(configuration: LocalConfiguration): Maybe<Long>

}