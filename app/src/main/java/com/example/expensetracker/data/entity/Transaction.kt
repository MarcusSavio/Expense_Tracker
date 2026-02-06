package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["accountId"]), Index(value = ["categoryId"]), Index(value = ["date"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val accountId: Long,
    val categoryId: Long?,
    val type: TransactionType,
    val amount: Double,
    val currency: String,
    val date: Long, // Timestamp
    val description: String? = null,
    val notes: String? = null,
    val tags: String? = null, // Comma-separated tags
    val receiptPath: String? = null, // Local file path
    val isRecurring: Boolean = false,
    val recurringPattern: String? = null, // JSON string for recurring pattern
    val isSplit: Boolean = false,
    val splitTransactionId: Long? = null, // Reference to parent split transaction
    val createdAt: Long = System.currentTimeMillis()
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER // Internal transfer between accounts
}
