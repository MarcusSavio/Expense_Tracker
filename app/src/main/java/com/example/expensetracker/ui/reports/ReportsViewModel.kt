package com.example.expensetracker.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.database.DatabaseModule
import com.example.expensetracker.data.entity.Goal
import com.example.expensetracker.data.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseModule.getDatabase(application)
    private val goalRepository = GoalRepository(db.goalDao())

    private val _activeGoals = MutableStateFlow<List<Goal>>(emptyList())
    val activeGoals: StateFlow<List<Goal>> = _activeGoals.asStateFlow()

    private val _completedGoals = MutableStateFlow<List<Goal>>(emptyList())
    val completedGoals: StateFlow<List<Goal>> = _completedGoals.asStateFlow()

    init {
        viewModelScope.launch {
            goalRepository.getActiveGoals().collect { _activeGoals.value = it }
        }
        viewModelScope.launch {
            goalRepository.getCompletedGoals().collect { _completedGoals.value = it }
        }
    }
}

