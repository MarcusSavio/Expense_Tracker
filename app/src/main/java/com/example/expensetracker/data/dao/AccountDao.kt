package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE isArchived = 0 ORDER BY name ASC")
    fun getAllAccounts(): Flow<List<Account>>
    
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): Account?
    
    @Query("SELECT * FROM accounts WHERE type = :type AND isArchived = 0 ORDER BY name ASC")
    fun getAccountsByType(type: String): Flow<List<Account>>
    
    @Query("SELECT * FROM accounts WHERE isArchived = 0")
    suspend fun getAllAccountsSync(): List<Account>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account): Long
    
    @Update
    suspend fun updateAccount(account: Account)
    
    @Delete
    suspend fun deleteAccount(account: Account)
    
    @Query("UPDATE accounts SET isArchived = 1 WHERE id = :id")
    suspend fun archiveAccount(id: Long)
    
    @Query("UPDATE accounts SET currentBalance = :balance, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateBalance(id: Long, balance: Double, timestamp: Long = System.currentTimeMillis())
}
