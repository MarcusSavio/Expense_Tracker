package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.GoalDao
import com.example.expensetracker.data.entity.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: GoalDao) {
    fun getActiveGoals(): Flow<List<Goal>> = goalDao.getActiveGoals()
    
    fun getCompletedGoals(): Flow<List<Goal>> = goalDao.getCompletedGoals()
    
    fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()
    
    suspend fun getGoalById(id: Long): Goal? = goalDao.getGoalById(id)
    
    suspend fun insertGoal(goal: Goal): Long = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
    
    suspend fun updateGoalProgress(id: Long, amount: Double) {
        goalDao.updateGoalProgress(id, amount, System.currentTimeMillis())
    }
    
    suspend fun markGoalCompleted(id: Long) {
        goalDao.markGoalCompleted(id, System.currentTimeMillis())
    }
}
