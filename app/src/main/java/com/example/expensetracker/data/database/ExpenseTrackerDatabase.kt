package com.example.expensetracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.data.dao.*
import com.example.expensetracker.data.entity.*

@Database(
    entities = [
        Account::class,
        Category::class,
        Transaction::class,
        Investment::class,
        Goal::class,
        Reminder::class,
        AppSettings::class,
        AccountBalanceHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun investmentDao(): InvestmentDao
    abstract fun goalDao(): GoalDao
    abstract fun reminderDao(): ReminderDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun accountBalanceHistoryDao(): AccountBalanceHistoryDao
}
