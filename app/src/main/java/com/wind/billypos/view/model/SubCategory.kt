package com.wind.billypos.view.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class SubCategory(
    val id: Long? = null,
    val idCompany: Long? = 0,
    val id_category: Long? = 0,
    val subcategoryName: String? = null,
    val subcategoryOrder: Int? = null,
    val status: Boolean? = true,
    val isActive: Boolean? = true,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
): Error(), Parcelable{

    fun isSubCategoryNameIsIncorrect() = subcategoryName.isNullOrEmpty()
    fun areDataCorrect() = !isSubCategoryNameIsIncorrect()

}