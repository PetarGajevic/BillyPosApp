package com.wind.billypos.view.model

import android.os.Parcelable
import com.wind.billypos.base.Error
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime


@Parcelize
data class Operator(
    val id: Long? = null,
    val businUnit: Long? = null,
    val cashRegister: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val operatorCode: String? = null,
    val pin: String? = null,
    val nationalIdNo: String? = null,
    val userRole: String? = "OPERATOR_MOBILE",
    val permissions: List<String>? = listOf(),
    val status: Boolean? = true,
    val isActive: Boolean? = true,
    val createdAt: LocalDateTime? = LocalDateTime.now()
): Error(), Parcelable{

    fun isFirstNameIncorrect() = firstName.isNullOrEmpty()
    fun isLastNameIncorrect() = lastName.isNullOrEmpty()
    fun isOperatorCodeIncorrect() = operatorCode.isNullOrEmpty()
    fun isPinIncorrect() = pin.isNullOrEmpty()
    fun isPinLengthIncorrect() = pin?.length != 4
    fun isNationalIdNoIncorrect() = nationalIdNo.isNullOrEmpty()
    fun areDataCorrect() = !isFirstNameIncorrect() && !isLastNameIncorrect() && !isOperatorCodeIncorrect() && !isPinIncorrect() && !isPinLengthIncorrect() && !isNationalIdNoIncorrect()

}