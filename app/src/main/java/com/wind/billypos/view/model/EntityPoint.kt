package com.wind.billypos.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class EntityPoint(
    val id: Long? = null,
    val uuid: String? = "",
    val entityName: String? = "",
    val entityColor: String? = "",
    val idBusinessUnit: Int? = 0,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    var isOpen: Boolean? = true
): Parcelable