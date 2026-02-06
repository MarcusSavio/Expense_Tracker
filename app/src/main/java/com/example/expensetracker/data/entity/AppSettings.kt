package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey
    val key: String,
    val value: String
)

// Settings keys
object SettingsKeys {
    const val DEFAULT_CURRENCY = "default_currency"
    const val DECIMAL_PRECISION = "decimal_precision"
    const val MONTH_START_DAY = "month_start_day" // 1-31
    const val APP_LOCK_ENABLED = "app_lock_enabled"
    const val APP_LOCK_TYPE = "app_lock_type" // PIN, PATTERN, BIOMETRIC
    const val AUTO_LOCK_TIMER = "auto_lock_timer" // Minutes
    const val HIDE_BALANCES = "hide_balances"
    const val DARK_MODE = "dark_mode"
    const val FIRE_SWR = "fire_safe_withdrawal_rate" // Default 4.0
    const val LAST_BACKUP_TIME = "last_backup_time"
}
