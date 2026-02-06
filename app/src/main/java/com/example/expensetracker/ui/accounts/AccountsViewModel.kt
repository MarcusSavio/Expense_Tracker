package com.example.expensetracker.ui.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.database.DatabaseModule
import com.example.expensetracker.data.entity.Account
import com.example.expensetracker.data.repository.AccountRepository
import com.example.expensetracker.data.repository.InvestmentRepository
import com.example.expensetracker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class AccountsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = DatabaseModule.getDatabase(application)
    private val accountRepository = AccountRepository(
        db.accountDao(),
        db.accountBalanceHistoryDao()
    )
    private val investmentRepository = InvestmentRepository(db.investmentDao())
    private val settingsRepository = SettingsRepository(db.appSettingsDao())

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _totalBalance = MutableStateFlow("$0.00")
    val totalBalance: StateFlow<String> = _totalBalance.asStateFlow()

    private val _netWorth = MutableStateFlow("$0.00")
    val netWorth: StateFlow<String> = _netWorth.asStateFlow()

    private val _hideBalances = MutableStateFlow(false)
    val hideBalances: StateFlow<Boolean> = _hideBalances.asStateFlow()

    private val _currency = MutableStateFlow("USD")
    val currency: StateFlow<String> = _currency.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getSettingValue(com.example.expensetracker.data.entity.SettingsKeys.HIDE_BALANCES)?.toBoolean()?.let {
                _hideBalances.value = it
            }
            _currency.value = settingsRepository.getDefaultCurrency()
        }
        loadAccounts()
    }

    fun loadAccounts() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { list ->
                _accounts.value = list
                updateTotals(list)
            }
        }
    }

    private suspend fun updateTotals(accounts: List<Account>) {
        val total = accounts.sumOf { it.currentBalance }
        val invTotal = investmentRepository.getTotalCurrentValue(_currency.value)
        val net = total + invTotal
        val nf = NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
        _totalBalance.value = nf.format(total)
        _netWorth.value = nf.format(net)
    }

    fun setHideBalances(hide: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSetting(
                com.example.expensetracker.data.entity.SettingsKeys.HIDE_BALANCES,
                hide.toString()
            )
            _hideBalances.value = hide
        }
    }

    fun togglePrivacy() {
        _hideBalances.value = !_hideBalances.value
        setHideBalances(_hideBalances.value)
    }

    fun formatBalance(amount: Double): String {
        if (_hideBalances.value) return "••••••"
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
    }
}
