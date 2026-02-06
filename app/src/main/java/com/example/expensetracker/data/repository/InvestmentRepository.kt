package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.InvestmentDao
import com.example.expensetracker.data.entity.Investment
import kotlinx.coroutines.flow.Flow

class InvestmentRepository(private val investmentDao: InvestmentDao) {
    fun getAllInvestments(): Flow<List<Investment>> = investmentDao.getAllInvestments()
    
    suspend fun getInvestmentById(id: Long): Investment? = investmentDao.getInvestmentById(id)
    
    fun getInvestmentsByType(type: String): Flow<List<Investment>> = 
        investmentDao.getInvestmentsByType(type)
    
    fun getInvestmentsByAccount(accountName: String): Flow<List<Investment>> = 
        investmentDao.getInvestmentsByAccount(accountName)
    
    fun getAllInvestmentAccounts(): Flow<List<String>> = investmentDao.getAllInvestmentAccounts()
    
    fun getAllInvestmentTypes(): Flow<List<String>> = investmentDao.getAllInvestmentTypes()
    
    suspend fun insertInvestment(investment: Investment): Long = 
        investmentDao.insertInvestment(investment)
    
    suspend fun updateInvestment(investment: Investment) {
        investmentDao.updateInvestment(investment.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteInvestment(investment: Investment) = investmentDao.deleteInvestment(investment)
    
    suspend fun getTotalInvested(currency: String): Double {
        return investmentDao.getTotalInvested(currency) ?: 0.0
    }
    
    suspend fun getTotalCurrentValue(currency: String): Double {
        return investmentDao.getTotalCurrentValue(currency) ?: 0.0
    }
}
