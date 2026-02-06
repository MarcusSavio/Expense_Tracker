package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE isCompleted = 0 ORDER BY targetDate ASC, createdAt ASC")
    fun getActiveGoals(): Flow<List<Goal>>
    
    @Query("SELECT * FROM goals WHERE isCompleted = 1 ORDER BY updatedAt DESC")
    fun getCompletedGoals(): Flow<List<Goal>>
    
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<Goal>>
    
    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: Long): Goal?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal): Long
    
    @Update
    suspend fun updateGoal(goal: Goal)
    
    @Delete
    suspend fun deleteGoal(goal: Goal)
    
    @Query("UPDATE goals SET currentAmount = :amount, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateGoalProgress(id: Long, amount: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE goals SET isCompleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun markGoalCompleted(id: Long, timestamp: Long = System.currentTimeMillis())
}
