package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investments")
data class Investment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val accountName: String, // Investment account name (e.g., "Stock Portfolio", "Crypto Wallet")
    val assetName: String, // Name of the asset (e.g., "AAPL", "Bitcoin", "Gold")
    val assetType: InvestmentType,
    val amountInvested: Double,
    val currency: String,
    val investmentDate: Long, // Timestamp
    val currentValue: Double? = null, // Optional current value (manual entry)
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class InvestmentType {
    STOCK,
    ETF,
    MUTUAL_FUND,
    CRYPTO,
    GOLD,
    OTHER
}
