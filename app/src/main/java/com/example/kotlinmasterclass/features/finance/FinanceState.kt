package com.example.kotlinmasterclass.features.finance

data class FinanceState(
    val totalBalance: Float = 0f,
    val dailyChange: Float = 0f,
    val assets: List<Asset> = emptyList(),
    val transactions: List<Transaction> = emptyList()
)