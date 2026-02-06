package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val currency: String, // ISO currency code (USD, INR, EUR, etc.)
    val openingBalance: Double,
    val currentBalance: Double,
    val notes: String? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AccountType {
    SAVINGS,
    CURRENT,
    CREDIT,
    CASH
}
