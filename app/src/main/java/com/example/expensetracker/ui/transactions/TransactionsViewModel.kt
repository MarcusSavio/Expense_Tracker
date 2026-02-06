package com.example.expensetracker.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Transactions - Coming Soon"
    }
    val text: LiveData<String> = _text
}
