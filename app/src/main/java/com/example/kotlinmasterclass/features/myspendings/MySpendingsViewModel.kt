package com.example.kotlinmasterclass.features.myspendings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Videocam
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MySpendingsViewModel @Inject constructor() : ViewModel() {

    val spendingOverviewList = listOf(
        SpendingOverviewTypes(1, Icons.Default.Fastfood, "Food", "$42.50", 0xFFE57373, 0xFFFFCDD2),
        SpendingOverviewTypes(2, Icons.Default.DirectionsBus, "Transport", "$12.00", 0xFF757575, 0xFFE0E0E0),
        SpendingOverviewTypes(3, Icons.Default.ShoppingBag, "Shopping", "$120.99", 0xFF81C784, 0xFFC8E6C9),
        SpendingOverviewTypes(4, Icons.Default.CarRental, "Rent", "$1200.00", 0xFFFBC02D, 0xFFFFF9C4),
        SpendingOverviewTypes(5, Icons.Default.LiveTv, "Entertainment", "$15.50", 0xFFF06292, 0xFFF8BBD0),
        SpendingOverviewTypes(6, Icons.Default.MedicalServices, "Health", "$50.00", 0xFF4DD0E1, 0xFFB2EBF2)
    )

    val recentTransactionsList = listOf(
        RecentTransactionData(1, Icons.Default.LocalPizza, "Pizza Hut", "Food & Drinks", "-$25.00", "Today", 0xFF81C784, 0xFFC8E6C9),
        RecentTransactionData(2, Icons.Default.LocalTaxi, "Uber", "Transport", "-$12.00", "Yesterday", 0xFF4DD0E1, 0xFFB2EBF2),
        RecentTransactionData(3, Icons.Default.ShoppingCart, "Amazon", "Shopping", "-$89.99", "14 June", 0xFFE57373, 0xFFFFCDD2),
        RecentTransactionData(4, Icons.Default.Videocam, "Netflix", "Entertainment", "-$15.50", "12 June", 0xFFF06292, 0xFFF8BBD0),
        RecentTransactionData(5, Icons.Default.LocalPharmacy, "Pharmacy", "Health", "-$30.00", "10 June", 0xFFFBC02D, 0xFFFFF9C4),
        RecentTransactionData(6, Icons.Default.Coffee, "Starbucks", "Food & Drinks", "-$5.50", "09 June", 0xFF757575, 0xFFE0E0E0)
    )

}