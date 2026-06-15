package com.example.kotlinmasterclass.features.myspendings

import androidx.compose.ui.graphics.vector.ImageVector

data class SpendingOverviewTypes(
    val id: Int,
    val icon: ImageVector,
    val category: String,
    val amount: String,
    val iconColor: Long,
    val backgroundColor: Long
)