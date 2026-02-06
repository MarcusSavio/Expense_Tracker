package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.data.database.DatabaseModule

class ExpenseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize database (lazy initialization)
        DatabaseModule.getDatabase(this)
    }
}
