package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, createdAt DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?
    
    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC, createdAt DESC")
    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getAccountTransactionsByDateRange(accountId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE isRecurring = 1")
    fun getRecurringTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE isSplit = 1 AND splitTransactionId IS NULL")
    fun getSplitTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE splitTransactionId = :parentId")
    suspend fun getSplitTransactionParts(parentId: Long): List<Transaction>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>)
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
    
    @Query("SELECT SUM(amount) FROM transactions WHERE accountId = :accountId AND type = 'EXPENSE' AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalExpensesForAccount(accountId: Long, startDate: Long, endDate: Long): Double?
    
    @Query("SELECT SUM(amount) FROM transactions WHERE accountId = :accountId AND type = 'INCOME' AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalIncomeForAccount(accountId: Long, startDate: Long, endDate: Long): Double?
}
