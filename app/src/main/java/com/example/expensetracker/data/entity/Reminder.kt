package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: ReminderType,
    val amount: Double? = null, // For bill reminders
    val currency: String? = null,
    val dueDate: Long, // Timestamp
    val repeatPattern: String? = null, // JSON string for repeat schedule (daily, weekly, monthly, yearly)
    val notes: String? = null,
    val personName: String? = null, // For person reminders
    val isPaid: Boolean = false,
    val paidDate: Long? = null,
    val isSkipped: Boolean = false,
    val skipDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ReminderType {
    BILL, // Bill reminder (rent, utilities, subscriptions)
    PERSON // Person reminder (owed/owing)
}
