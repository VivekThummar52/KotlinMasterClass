package com.example.kotlinmasterclass.core.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    /**
     * Safely converts a Double to a BigDecimal to prevent floating-point errors.
     */
    fun toSafeDecimal(value: Double): BigDecimal {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_EVEN)
    }

    /**
     * Formats a BigDecimal directly into a localized currency string (e.g., "$204.97").
     */
    fun format(amount: BigDecimal): String {
        // Use your preferred Locale here. US formats as $0.00
//        val format = NumberFormat.getCurrencyInstance(Locale.US)
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return format.format(amount)
    }

    /**
     * Formats a raw Double into a localized currency string safely.
     */
    fun format(amount: Double): String {
        return format(toSafeDecimal(amount))
    }
}