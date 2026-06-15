package com.example.kotlinmasterclass.features.myspendings

import androidx.compose.ui.graphics.vector.ImageVector

data class RecentTransactionData(
    val id: Int,
    val icon: ImageVector,
    val name: String,
    val category: String,
    val amount: String,
    val date: String,
    val iconColor: Long,
    val backgroundColor: Long
)