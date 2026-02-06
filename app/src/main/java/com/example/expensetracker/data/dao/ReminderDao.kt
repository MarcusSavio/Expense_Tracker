package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE isPaid = 0 AND isSkipped = 0 ORDER BY dueDate ASC")
    fun getActiveReminders(): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE dueDate >= :startDate AND dueDate <= :endDate AND isPaid = 0 AND isSkipped = 0 ORDER BY dueDate ASC")
    fun getRemindersByDateRange(startDate: Long, endDate: Long): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE type = :type AND isPaid = 0 AND isSkipped = 0 ORDER BY dueDate ASC")
    fun getActiveRemindersByType(type: String): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): Reminder?
    
    @Query("SELECT * FROM reminders ORDER BY dueDate DESC")
    fun getAllReminders(): Flow<List<Reminder>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long
    
    @Update
    suspend fun updateReminder(reminder: Reminder)
    
    @Delete
    suspend fun deleteReminder(reminder: Reminder)
    
    @Query("UPDATE reminders SET isPaid = 1, paidDate = :timestamp, updatedAt = :timestamp WHERE id = :id")
    suspend fun markPaid(id: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE reminders SET isSkipped = 1, skipDate = :timestamp, updatedAt = :timestamp WHERE id = :id")
    suspend fun markSkipped(id: Long, timestamp: Long = System.currentTimeMillis())
}
