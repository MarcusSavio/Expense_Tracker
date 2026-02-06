package com.example.expensetracker.ui.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Reports - Coming Soon"
    }
    val text: LiveData<String> = _text
}
