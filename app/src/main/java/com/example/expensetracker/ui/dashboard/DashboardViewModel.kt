package com.example.expensetracker.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.database.DatabaseModule
import com.example.expensetracker.data.entity.SettingsKeys
import com.example.expensetracker.data.entity.TransactionType
import com.example.expensetracker.data.repository.AccountRepository
import com.example.expensetracker.data.repository.InvestmentRepository
import com.example.expensetracker.data.repository.SettingsRepository
import com.example.expensetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseModule.getDatabase(application)
    private val accountRepository = AccountRepository(
        db.accountDao(),
        db.accountBalanceHistoryDao()
    )
    private val transactionRepository = TransactionRepository(
        db.transactionDao(),
        db.accountDao()
    )
    private val investmentRepository = InvestmentRepository(db.investmentDao())
    private val settingsRepository = SettingsRepository(db.appSettingsDao())

    private val _currency = MutableStateFlow("USD")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _hideBalances = MutableStateFlow(false)
    val hideBalances: StateFlow<Boolean> = _hideBalances.asStateFlow()

    private val _totalBalance = MutableStateFlow("$0.00")
    val totalBalance: StateFlow<String> = _totalBalance.asStateFlow()

    private val _netWorth = MutableStateFlow("$0.00")
    val netWorth: StateFlow<String> = _netWorth.asStateFlow()

    private val _monthlyIncome = MutableStateFlow("$0.00")
    val monthlyIncome: StateFlow<String> = _monthlyIncome.asStateFlow()

    private val _monthlyExpense = MutableStateFlow("$0.00")
    val monthlyExpense: StateFlow<String> = _monthlyExpense.asStateFlow()

    private val _monthlyNet = MutableStateFlow("$0.00")
    val monthlyNet: StateFlow<String> = _monthlyNet.asStateFlow()

    private val _fireNumber = MutableStateFlow("$0.00")
    val fireNumber: StateFlow<String> = _fireNumber.asStateFlow()

    private val _fireProgress = MutableStateFlow("0%")
    val fireProgress: StateFlow<String> = _fireProgress.asStateFlow()

    init {
        viewModelScope.launch {
            _currency.value = settingsRepository.getDefaultCurrency()
            settingsRepository.getSettingValue(SettingsKeys.HIDE_BALANCES)?.toBoolean()?.let {
                _hideBalances.value = it
            }
        }

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val monthStart = getMonthStartMillis()

            combine(
                accountRepository.getAllAccounts(),
                transactionRepository.getAllTransactions()
            ) { accounts, transactions ->
                Triple(accounts, transactions, now to monthStart)
            }.collect { (accounts, transactions, range) ->
                val (nowTs, startTs) = range

                val totalAccountBalance = accounts.sumOf { it.currentBalance }
                val nf = currencyFormatter()
                _totalBalance.value = nf.format(totalAccountBalance)

                val investedTotal = investmentRepository.getTotalCurrentValue(_currency.value)
                val net = totalAccountBalance + investedTotal
                _netWorth.value = nf.format(net)

                val monthly = transactions.filter { it.date in startTs..nowTs }
                val income = monthly
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }
                val expense = monthly
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                _monthlyIncome.value = nf.format(income)
                _monthlyExpense.value = nf.format(expense)
                _monthlyNet.value = nf.format(income - expense)

                val swrPercent = settingsRepository.getFireSWR() // e.g. 4.0
                val swrRate = if (swrPercent > 0) swrPercent / 100.0 else 0.0
                val annualExpense = expense * 12.0
                val fireNumberRaw = if (swrRate > 0) annualExpense / swrRate else 0.0
                _fireNumber.value = nf.format(fireNumberRaw)

                val progress = if (fireNumberRaw > 0) {
                    (investedTotal / fireNumberRaw * 100.0).coerceIn(0.0, 999.9)
                } else {
                    0.0
                }
                _fireProgress.value = "${"%.1f".format(progress)}%"
            }
        }
    }

    fun togglePrivacy() {
        viewModelScope.launch {
            val newValue = !_hideBalances.value
            _hideBalances.value = newValue
            settingsRepository.setSetting(SettingsKeys.HIDE_BALANCES, newValue.toString())
        }
    }

    private fun getMonthStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun currencyFormatter(): NumberFormat =
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
}

