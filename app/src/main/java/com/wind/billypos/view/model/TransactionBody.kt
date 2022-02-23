package com.wind.billypos.view.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import java.util.*

@Parcelize
data class TransactionBody(
    val id: Long? = null,
    val uuid: String? = UUID.randomUUID().toString(),
    val idTransactionHead: String? = null,
    val idCompany: Long? = null,
    val idAddress: Long? = null,
    val idDevice: Long? = null,
    val idUser: Long? = null,
    val idItem: String? = null,
    val itemName: String? = null,
    val itemUnit: String? = null,
    val idCategory: Long? = null,
    val unitPrice: Double? = 0.0,
    val unitPriceWithVat: Double? = 0.0,
    val rabat: Double? = 0.0,
    var quantity: Double? = 0.0,
    val vatRate: Double? = 0.0,
    val vatAmount: Double? = 0.0,
    val itemPrice: Double? = 0.0,
    val itemPriceWithDiscount: Double = 0.0,
    val total: Double? = 0.0,
    val totalWithVat: Double? = 0.0,
    val costPrice: Double? = 0.0,
    val lastServerSync: LocalDateTime? = null,
    val item: Item? = null
): Error(), Parcelable{

//    fun upb() = upa()?.div((1+ vatRate?.div(100)!!))?.times(1-0/100)
//    fun upa() = itemPriceWithVat?.div(quantity!!)


//    fun upa() = itemPriceWithDiscount?.times(1+ vatRate?.div(100)!!)?.times((1-0/10))
//    fun pb() = unitPrice?.times(1-0/100)?.times(quantity!!)
//    fun va() = pb()?.times(vatRate?.div(100)!!)
//    fun pa() = pb()?.plus(va()!!)

    fun upa() = itemPriceWithDiscount
    fun upb() = upa()?.div(((1+ (vatRate?.div(100) ?: 0.0)))*(1- (rabat?.div(100) ?: 0.0)))
    fun pa() = upa()?.times(quantity ?: 0.0)
    fun pb() = pa()?.minus(vatAmount ?: unitPrice!!)

}