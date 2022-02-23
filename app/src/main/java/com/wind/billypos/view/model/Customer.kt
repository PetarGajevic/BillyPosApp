package com.wind.billypos.view.model

import android.os.Parcelable
import com.wind.billypos.base.Error
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import java.util.*

@Parcelize
data class Customer(
    val id: Long? = null,
    val uuid: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val idType: String? = null,
    val idNum: String? = null,
    val idCompany: Long? = null,
    val deviceId: Long? = null,
    val userID: Long? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastServerSync: LocalDateTime? = null,
    val isSync: Boolean? = false
) : Error(), Parcelable
{

    fun isCustomerNameIncorrect() = name.isNullOrEmpty()
    fun isCustomerNUISIncorrect() = idNum.isNullOrEmpty()
    fun isCustomerEmailIncorrect() = email.isNullOrEmpty()
    fun areDataCorrect() = !isCustomerNameIncorrect() && !isCustomerNUISIncorrect() && !isCustomerEmailIncorrect()
}