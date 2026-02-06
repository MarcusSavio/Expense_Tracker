package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Investment
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentDao {
    @Query("SELECT * FROM investments ORDER BY investmentDate DESC")
    fun getAllInvestments(): Flow<List<Investment>>
    
    @Query("SELECT * FROM investments WHERE id = :id")
    suspend fun getInvestmentById(id: Long): Investment?
    
    @Query("SELECT * FROM investments WHERE assetType = :type ORDER BY investmentDate DESC")
    fun getInvestmentsByType(type: String): Flow<List<Investment>>
    
    @Query("SELECT * FROM investments WHERE accountName = :accountName ORDER BY investmentDate DESC")
    fun getInvestmentsByAccount(accountName: String): Flow<List<Investment>>
    
    @Query("SELECT DISTINCT accountName FROM investments")
    fun getAllInvestmentAccounts(): Flow<List<String>>
    
    @Query("SELECT DISTINCT assetType FROM investments")
    fun getAllInvestmentTypes(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: Investment): Long
    
    @Update
    suspend fun updateInvestment(investment: Investment)
    
    @Delete
    suspend fun deleteInvestment(investment: Investment)
    
    @Query("SELECT SUM(amountInvested) FROM investments WHERE currency = :currency")
    suspend fun getTotalInvested(currency: String): Double?
    
    @Query("SELECT SUM(COALESCE(currentValue, amountInvested)) FROM investments WHERE currency = :currency")
    suspend fun getTotalCurrentValue(currency: String): Double?
}
