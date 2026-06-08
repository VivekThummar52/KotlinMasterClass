package com.example.kotlinmasterclass.features.finance

data class Asset(
    val symbol: String,
    val name: String,
    val holdings: Float,
    val currentPrice: Float,
    val priceHistory: List<Float>
) {
    val totalValue: Float get() = holdings * currentPrice
    val isPositive: Boolean get() = priceHistory.lastOrNull() ?: 0f >= (priceHistory.dropLast(1).lastOrNull() ?: 0f)
}