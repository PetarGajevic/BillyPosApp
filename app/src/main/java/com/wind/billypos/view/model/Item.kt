package com.wind.billypos.view.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import com.wind.billypos.utils.formatPrice
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import java.util.*

@Parcelize
data class Item(
    val id: Long = 0,
    val uuid: String = UUID.randomUUID().toString(),
    val idCompany: Long? = 0,
    val idAddress: Long? = 0,
    val idCategory: Long? = 0,
    val idSubcategory: Long? = 0,
    val itemName: String? = null,
    val itemDesc: String? = null,
    val barcode: String? = null,
    val itemImage: String? = null,
    val itemUnit: String? = null,
    val itemType: String? = null,
    val itemPrice: Double = 0.0,
    val itemPriceWithDiscount: Double = 0.0,
    val unitPrice: Double = 0.0,
    val discount: Double = 0.0,
    val vat: Double? = 0.0,
    val status: Int? = 0,
    val isActive: Boolean? = true,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val categoryColor: String? = "#3B4453",
    val lastServerSync: LocalDateTime? = null,
    val isSync: Boolean? = false
): Error(), Parcelable{

    fun isItemNameIncorrect() = itemName.isNullOrEmpty()
    fun isItemPriceIncorrect() = itemPrice <= 0.0
    fun isItemPriceWithDiscountIncorrect() = itemPriceWithDiscount.isNaN()
    fun isItemBarcodeIncorrect() = barcode.isNullOrEmpty()
    fun isItemUnitIncorrect() = itemUnit.isNullOrEmpty()
    fun areDataCorrect() = !isItemUnitIncorrect() && !isItemBarcodeIncorrect() && !isItemPriceWithDiscountIncorrect() && !isItemPriceIncorrect() && !isItemNameIncorrect()


    fun itemPriceWithVat() = itemPrice.times(1 + (vat!!/100))


    fun itemPriceWithDiscountFormat() = itemPriceWithDiscount.formatPrice()
    fun itemPriceFormat() = itemPrice.formatPrice()
    fun unitPriceFormat() = unitPrice.formatPrice()
    fun itemDiscountFormat() = discount.formatPrice()
    fun itemDiscountWithPercentageFormat() = discount.formatPrice() + "%"

}

