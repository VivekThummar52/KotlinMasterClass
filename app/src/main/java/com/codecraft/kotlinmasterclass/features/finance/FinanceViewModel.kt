package com.codecraft.kotlinmasterclass.features.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

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

data class FinanceState(
    val totalBalance: Float = 0f,
    val dailyChange: Float = 0f,
    val assets: List<Asset> = emptyList(),
    val transactions: List<Transaction> = emptyList()
)

data class Transaction(val id: String, val title: String, val amount: Float, val timestamp: String)

@HiltViewModel
class FinanceViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceState())
    val uiState = _uiState.asStateFlow()

    init {
        initializePortfolio()
        startMarketSimulation()
    }

    private fun initializePortfolio() {
        val initialAssets = listOf(
            Asset("KTLN", "Kotlin Multiplatform", 150f, 124.50f, List(20) { 120f + Random.nextFloat() * 10f }),
            Asset("CMPZ", "Compose UI Corp", 420f, 64.20f, List(20) { 60f + Random.nextFloat() * 8f }),
            Asset("CRTN", "Coroutine Dynamics", 85f, 310.80f, List(20) { 300f + Random.nextFloat() * 20f })
        )
        
        val initialTransactions = listOf(
            Transaction("1", "Deposit from Varachha Bank", 5000f, "Today, 09:41 AM"),
            Transaction("2", "Mechanical Keyboard", -145.99f, "Yesterday, 14:20 PM"),
            Transaction("3", "Coffee Shop", -4.50f, "Yesterday, 08:15 AM")
        )

        updateState(initialAssets, initialTransactions)
    }

    private fun startMarketSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(1200) // Market ticks every 1.2 seconds
                
                _uiState.update { state ->
                    val updatedAssets = state.assets.map { asset ->
                        // Random Walk Algorithm: Price shifts up or down by max 2%
                        val shift = asset.currentPrice * (Random.nextFloat() * 0.04f - 0.02f)
                        val newPrice = (asset.currentPrice + shift).coerceAtLeast(1f)
                        
                        // Keep a rolling history of exactly 30 data points for the live chart
                        val newHistory = (asset.priceHistory + newPrice).takeLast(30)
                        
                        asset.copy(currentPrice = newPrice, priceHistory = newHistory)
                    }
                    
                    val newBalance = updatedAssets.map { it.totalValue }.sum()
                    val oldBalance = state.assets.map { it.totalValue }.sum()
                    val change = newBalance - (state.totalBalance - state.dailyChange) // Mocking day start

                    state.copy(
                        assets = updatedAssets,
                        totalBalance = newBalance,
                        dailyChange = change
                    )
                }
            }
        }
    }

    private fun updateState(assets: List<Asset>, transactions: List<Transaction>) {
        val balance = assets.map { it.totalValue }.sum()
        _uiState.update { it.copy(assets = assets, transactions = transactions, totalBalance = balance) }
    }
}