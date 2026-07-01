package com.codecraft.kotlinmasterclass.features.wallet

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class Transaction(val title: String, val date: String, val amount: String, val isPositive: Boolean = false)

@HiltViewModel
class WalletViewModel @Inject constructor() : ViewModel() {
    val balance = "₹12,482.50"
    val cardNumber = "•••• •••• •••• 4209"
    val cardHolder = "VIVEK THUMMAR"

    val recentTransactions = listOf(
        Transaction("Apple Store", "Today, 2:45 PM", "-₹1,299.00"),
        Transaction("Salary Deposit", "Yesterday", "+₹4,500.00", true),
        Transaction("Uber Rides", "June 3", "-₹24.50"),
//        Transaction("Coffee Shop", "June 2", "-₹4.20"),
//        Transaction("Steam Games", "June 1", "-₹59.99")
    )
}