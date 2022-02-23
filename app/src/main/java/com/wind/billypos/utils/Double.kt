package com.wind.billypos.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


fun Double.formatPrice(): String {
    val otherSymbols = DecimalFormatSymbols()
    otherSymbols.decimalSeparator = ','
    otherSymbols.groupingSeparator = '.'
    val decimalFormat = DecimalFormat("#,##0.00", otherSymbols)
    return decimalFormat.format(this)
}

fun Double.formatReceipt(): String {
    val otherSymbols = DecimalFormatSymbols()
    otherSymbols.decimalSeparator = '.'
    val decimalFormat = DecimalFormat("0.00", otherSymbols)
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(this)
}

fun Double.formatQuantity(): String {
    val otherSymbols = DecimalFormatSymbols()
    otherSymbols.decimalSeparator = '.'
    otherSymbols.groupingSeparator = ','
    val decimalFormat = DecimalFormat("0.000", otherSymbols)
    return decimalFormat.format(this)
}

fun Double.formatPriceForTax(): String {
    val otherSymbols = DecimalFormatSymbols()
    otherSymbols.decimalSeparator = '.'
    val decimalFormat = DecimalFormat("0.0000", otherSymbols)
    return decimalFormat.format(this)
}

fun Double.formatFourDecimal(): Double {
    val df = DecimalFormat("#.####")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(this).toDouble()
}

fun Double.formatTwoDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(this).toDouble()
}




