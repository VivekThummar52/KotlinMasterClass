package com.example.kotlinmasterclass.features.motion

import androidx.lifecycle.ViewModel
import com.example.kotlinmasterclass.core.utils.CurrencyFormatter // ADD THIS IMPORT
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

data class ReceiptItem(val name: String, val price: Double)

@HiltViewModel
class MotionViewModel @Inject constructor() : ViewModel() {

    val transactionId = "TXN-8492-AB"
    val date = "June 4, 2026 - 12:05 PM"

    val items = listOf(
        ReceiptItem("Mechanical Keyboard", 129.99),
        ReceiptItem("Wireless Mouse", 49.99),
        ReceiptItem("Desk Mat", 19.99),
        ReceiptItem("Express Shipping", 5.00)
    )

    // THE FIX: Calculate the sum safely using BigDecimal arithmetic
    val total: BigDecimal = items.fold(BigDecimal.ZERO) { accumulator, item ->
        accumulator.add(BigDecimal.valueOf(item.price))
    }.setScale(2, RoundingMode.HALF_EVEN)

    // Optional: Pre-format the string here so the UI doesn't have to do any work
    val displayTotal: String = CurrencyFormatter.format(total)
}