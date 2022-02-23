package com.wind.billypos.data.local.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wind.billypos.data.local.model.LocalSubCategory
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionPayment


/**
 * Created by Sinan Dizdarevic on 2019-10-16.
 */
class ListConverter {

    private val gson = Gson()

    /**
     * List of String converter
     *
     * @param listOfString
     * @return
     */
    @TypeConverter
    fun fromStringList(listOfString: String?): MutableList<String> {
        return when {
            listOfString.isNullOrEmpty() -> mutableListOf()
            listOfString.isNotEmpty() -> gson.fromJson(
                listOfString,
                object : TypeToken<List<String>>() {}.type
            )
            else -> mutableListOf()
        }
    }

    @TypeConverter
    fun toStringList(listOfString: MutableList<String>?): String {
        return when {
            listOfString.isNullOrEmpty() -> ""
            listOfString.isNotEmpty() -> gson.toJson(listOfString)
            else -> ""
        }
    }

    /**
     * List of Transaction Body converter
     *
     * @param listOfTransactionBody
     * @return
     */
    @TypeConverter
    fun fromTransactionBodyArray(listOfTransactionBody: List<LocalTransactionBody>): String {
        return when {
            listOfTransactionBody.isNullOrEmpty() -> ""
            listOfTransactionBody.isNotEmpty() -> gson.toJson(listOfTransactionBody)
            else -> ""
        }
    }

    @TypeConverter
    fun toTransactionBodyArray(listOfTransactionBody: String): List<LocalTransactionBody> {
        return when {
            listOfTransactionBody.isEmpty() -> listOf()
            listOfTransactionBody.isNotEmpty() -> gson.fromJson(
                listOfTransactionBody,
                object : TypeToken<List<LocalTransactionBody>>() {}.type
            )
            else -> listOf()
        }
    }

    /**
     * List of Transaction Body converter
     *
     * @param listOfPaymentMethods
     * @return
     */
    @TypeConverter
    fun fromPaymentMethodArray(listOfTransactionPayment: List<LocalTransactionPayment>): String {
        return when {
            listOfTransactionPayment.isNullOrEmpty() -> ""
            listOfTransactionPayment.isNotEmpty() -> gson.toJson(listOfTransactionPayment)
            else -> ""
        }
    }

    @TypeConverter
    fun toPaymentMethodArray(listOfTransactionPayment: String): List<LocalTransactionPayment> {
        return when {
            listOfTransactionPayment.isEmpty() -> listOf()
            listOfTransactionPayment.isNotEmpty() -> gson.fromJson(
                listOfTransactionPayment,
                object : TypeToken<List<LocalTransactionPayment>>() {}.type
            )
            else -> listOf()
        }
    }

    /**
     * List of Transaction Body converter
     *
     * @param listOfSubCategories
     * @return
     */
    @TypeConverter
    fun fromSubCategoryArray(listOfSubCategories: List<LocalSubCategory>): String {
        return when {
            listOfSubCategories.isNullOrEmpty() -> ""
            listOfSubCategories.isNotEmpty() -> gson.toJson(listOfSubCategories)
            else -> ""
        }
    }

    @TypeConverter
    fun toSubCategoryArray(listOfSubCategories: String): List<LocalSubCategory> {
        return when {
            listOfSubCategories.isEmpty() -> listOf()
            listOfSubCategories.isNotEmpty() -> gson.fromJson(
                listOfSubCategories,
                object : TypeToken<List<LocalSubCategory>>() {}.type
            )
            else -> listOf()
        }
    }
//
//    /**
//     * List of Receipt Items converter
//     *
//     * @param listOfReceiptItems
//     * @return
//     */
//    @TypeConverter
//    fun fromReceiptItemArray(listOfReceiptItems: ArrayList<LocalReceipt.Item>): String {
//        return when {
//            listOfReceiptItems.isNullOrEmpty() -> ""
//            listOfReceiptItems.isNotEmpty() -> gson.toJson(listOfReceiptItems)
//            else -> ""
//        }
//    }
//
//    @TypeConverter
//    fun toReceiptItemArray(listOfReceiptItems: String): ArrayList<LocalReceipt.Item> {
//        return when {
//            listOfReceiptItems.isEmpty() -> arrayListOf()
//            listOfReceiptItems.isNotEmpty() -> gson.fromJson(
//                listOfReceiptItems,
//                object : TypeToken<List<LocalReceipt.Item>>() {}.type
//            )
//            else -> arrayListOf()
//        }
//    }


}