package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.AccountDao
import com.example.expensetracker.data.dao.AccountBalanceHistoryDao
import com.example.expensetracker.data.entity.Account
import com.example.expensetracker.data.entity.AccountBalanceHistory
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val accountDao: AccountDao,
    private val balanceHistoryDao: AccountBalanceHistoryDao
) {
    fun getAllAccounts(): Flow<List<Account>> = accountDao.getAllAccounts()
    
    suspend fun getAccountById(id: Long): Account? = accountDao.getAccountById(id)
    
    fun getAccountsByType(type: String): Flow<List<Account>> = accountDao.getAccountsByType(type)
    
    suspend fun getAllAccountsSync(): List<Account> = accountDao.getAllAccountsSync()
    
    suspend fun insertAccount(account: Account): Long {
        val id = accountDao.insertAccount(account)
        // Record initial balance in history
        balanceHistoryDao.insertBalanceHistory(
            AccountBalanceHistory(
                accountId = id,
                balance = account.currentBalance,
                date = System.currentTimeMillis(),
                notes = "Opening balance"
            )
        )
        return id
    }
    
    suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }
    
    suspend fun archiveAccount(id: Long) {
        accountDao.archiveAccount(id)
    }
    
    suspend fun updateBalance(id: Long, balance: Double, notes: String? = null) {
        val timestamp = System.currentTimeMillis()
        accountDao.updateBalance(id, balance, timestamp)
        balanceHistoryDao.insertBalanceHistory(
            AccountBalanceHistory(
                accountId = id,
                balance = balance,
                date = timestamp,
                notes = notes ?: "Balance update"
            )
        )
    }
    
    fun getBalanceHistory(accountId: Long): Flow<List<AccountBalanceHistory>> {
        return balanceHistoryDao.getBalanceHistoryForAccount(accountId)
    }
}
