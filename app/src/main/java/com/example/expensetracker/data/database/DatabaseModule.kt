package com.example.expensetracker.data.database

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.dao.*

object DatabaseModule {
    private const val DATABASE_NAME = "expense_tracker.db"
    private var instance: ExpenseTrackerDatabase? = null
    
    /**
     * Get database instance
     * For v1: Uses regular Room database
     * Database files are protected by Android's file system permissions (private to app)
     * Files stored in app's private directory cannot be accessed by other apps
     */
    fun getDatabase(context: Context): ExpenseTrackerDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                ExpenseTrackerDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration() // For development - remove in production
                .build()
            
            instance = db
            db
        }
    }
    
    fun closeDatabase() {
        instance?.close()
        instance = null
    }
}
