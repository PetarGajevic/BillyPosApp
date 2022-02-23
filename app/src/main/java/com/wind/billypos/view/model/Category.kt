package com.wind.billypos.view.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.threeten.bp.LocalDateTime
import java.util.*

@Parcelize
data class Category(
    val id: Long? = null,
    val idCompany: Long = 0,
    val categoryName: String? = null,
    val categoryColor: String? = null,
    val categoryOrder: Int? = null,
    val subcategories: List<SubCategory> = listOf(),
    val status: Boolean? = true,
    val isActive: Boolean? = true,
    val createdAt: LocalDateTime? = LocalDateTime.now()
): Error(), Parcelable {

    fun isCategoryNameIsIncorrect() = categoryName.isNullOrEmpty()
    fun areDataCorrect() = !isCategoryNameIsIncorrect()

    fun getCategoryId(): Long?{
        return id
    }

}