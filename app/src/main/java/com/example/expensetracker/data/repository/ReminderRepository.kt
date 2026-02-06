package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.ReminderDao
import com.example.expensetracker.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    fun getActiveReminders(): Flow<List<Reminder>> = reminderDao.getActiveReminders()
    
    fun getRemindersByDateRange(startDate: Long, endDate: Long): Flow<List<Reminder>> = 
        reminderDao.getRemindersByDateRange(startDate, endDate)
    
    fun getActiveRemindersByType(type: String): Flow<List<Reminder>> = 
        reminderDao.getActiveRemindersByType(type)
    
    suspend fun getReminderById(id: Long): Reminder? = reminderDao.getReminderById(id)
    
    fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminders()
    
    suspend fun insertReminder(reminder: Reminder): Long = reminderDao.insertReminder(reminder)
    
    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)
    
    suspend fun markPaid(id: Long) = reminderDao.markPaid(id, System.currentTimeMillis())
    
    suspend fun markSkipped(id: Long) = reminderDao.markSkipped(id, System.currentTimeMillis())
}
