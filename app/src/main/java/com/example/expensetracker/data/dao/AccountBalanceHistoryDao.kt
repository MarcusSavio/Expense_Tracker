package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.AccountBalanceHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountBalanceHistoryDao {
    @Query("SELECT * FROM account_balance_history WHERE accountId = :accountId ORDER BY date DESC")
    fun getBalanceHistoryForAccount(accountId: Long): Flow<List<AccountBalanceHistory>>
    
    @Query("SELECT * FROM account_balance_history WHERE accountId = :accountId AND date >= :startDate AND date <= :endDate ORDER BY date ASC")
    fun getBalanceHistoryByDateRange(accountId: Long, startDate: Long, endDate: Long): Flow<List<AccountBalanceHistory>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalanceHistory(history: AccountBalanceHistory)
    
    @Delete
    suspend fun deleteBalanceHistory(history: AccountBalanceHistory)
}
