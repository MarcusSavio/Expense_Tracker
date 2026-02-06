package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.TransactionDao
import com.example.expensetracker.data.dao.AccountDao
import com.example.expensetracker.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) {
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    
    suspend fun getTransactionById(id: Long): Transaction? = transactionDao.getTransactionById(id)
    
    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByAccount(accountId)
    
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByCategory(categoryId)
    
    fun getTransactionsByType(type: String): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByType(type)
    
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByDateRange(startDate, endDate)
    
    fun getAccountTransactionsByDateRange(
        accountId: Long, 
        startDate: Long, 
        endDate: Long
    ): Flow<List<Transaction>> = 
        transactionDao.getAccountTransactionsByDateRange(accountId, startDate, endDate)
    
    fun getRecurringTransactions(): Flow<List<Transaction>> = 
        transactionDao.getRecurringTransactions()
    
    suspend fun insertTransaction(transaction: Transaction): Long {
        val id = transactionDao.insertTransaction(transaction)
        // Update account balance
        updateAccountBalance(transaction)
        return id
    }
    
    suspend fun insertTransactions(transactions: List<Transaction>) {
        transactionDao.insertTransactions(transactions)
        transactions.forEach { updateAccountBalance(it) }
    }
    
    suspend fun updateTransaction(transaction: Transaction) {
        // Get old transaction to reverse balance change
        val oldTransaction = transactionDao.getTransactionById(transaction.id)
        transactionDao.updateTransaction(transaction)
        
        // Reverse old balance change and apply new one
        oldTransaction?.let { reverseAccountBalance(it) }
        updateAccountBalance(transaction)
    }
    
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
        reverseAccountBalance(transaction)
    }
    
    suspend fun deleteTransactionById(id: Long) {
        val transaction = transactionDao.getTransactionById(id)
        transaction?.let {
            transactionDao.deleteTransactionById(id)
            reverseAccountBalance(it)
        }
    }
    
    private suspend fun updateAccountBalance(transaction: Transaction) {
        val account = accountDao.getAccountById(transaction.accountId) ?: return
        
        val balanceChange = when (transaction.type) {
            com.example.expensetracker.data.entity.TransactionType.INCOME -> transaction.amount
            com.example.expensetracker.data.entity.TransactionType.EXPENSE -> -transaction.amount
            com.example.expensetracker.data.entity.TransactionType.TRANSFER -> 0.0 // Handled separately
        }
        
        if (balanceChange != 0.0) {
            val newBalance = account.currentBalance + balanceChange
            accountDao.updateBalance(account.id, newBalance, System.currentTimeMillis())
        }
    }
    
    private suspend fun reverseAccountBalance(transaction: Transaction) {
        val account = accountDao.getAccountById(transaction.accountId) ?: return
        
        val balanceChange = when (transaction.type) {
            com.example.expensetracker.data.entity.TransactionType.INCOME -> -transaction.amount
            com.example.expensetracker.data.entity.TransactionType.EXPENSE -> transaction.amount
            com.example.expensetracker.data.entity.TransactionType.TRANSFER -> 0.0
        }
        
        if (balanceChange != 0.0) {
            val newBalance = account.currentBalance + balanceChange
            accountDao.updateBalance(account.id, newBalance, System.currentTimeMillis())
        }
    }
    
    suspend fun getTotalExpensesForAccount(accountId: Long, startDate: Long, endDate: Long): Double {
        return transactionDao.getTotalExpensesForAccount(accountId, startDate, endDate) ?: 0.0
    }
    
    suspend fun getTotalIncomeForAccount(accountId: Long, startDate: Long, endDate: Long): Double {
        return transactionDao.getTotalIncomeForAccount(accountId, startDate, endDate) ?: 0.0
    }
}
