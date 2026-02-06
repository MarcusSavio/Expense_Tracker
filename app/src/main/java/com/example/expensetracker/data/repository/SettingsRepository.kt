package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.AppSettingsDao
import com.example.expensetracker.data.entity.AppSettings
import com.example.expensetracker.data.entity.SettingsKeys

class SettingsRepository(private val settingsDao: AppSettingsDao) {
    suspend fun getSetting(key: String): AppSettings? = settingsDao.getSetting(key)
    
    suspend fun getSettingValue(key: String): String? = settingsDao.getSettingValue(key)
    
    suspend fun setSetting(key: String, value: String) {
        settingsDao.insertSetting(AppSettings(key, value))
    }
    
    suspend fun updateSetting(setting: AppSettings) = settingsDao.updateSetting(setting)
    
    suspend fun deleteSetting(key: String) = settingsDao.deleteSetting(key)
    
    // Convenience methods for common settings
    suspend fun getDefaultCurrency(): String = getSettingValue(SettingsKeys.DEFAULT_CURRENCY) ?: "USD"
    
    suspend fun setDefaultCurrency(currency: String) = setSetting(SettingsKeys.DEFAULT_CURRENCY, currency)
    
    suspend fun getDecimalPrecision(): Int = 
        getSettingValue(SettingsKeys.DECIMAL_PRECISION)?.toIntOrNull() ?: 2
    
    suspend fun setDecimalPrecision(precision: Int) = 
        setSetting(SettingsKeys.DECIMAL_PRECISION, precision.toString())
    
    suspend fun isAppLockEnabled(): Boolean = 
        getSettingValue(SettingsKeys.APP_LOCK_ENABLED)?.toBoolean() ?: false
    
    suspend fun setAppLockEnabled(enabled: Boolean) = 
        setSetting(SettingsKeys.APP_LOCK_ENABLED, enabled.toString())
    
    suspend fun getAppLockType(): String = 
        getSettingValue(SettingsKeys.APP_LOCK_TYPE) ?: "PIN"
    
    suspend fun setAppLockType(type: String) = setSetting(SettingsKeys.APP_LOCK_TYPE, type)
    
    suspend fun isDarkModeEnabled(): Boolean = 
        getSettingValue(SettingsKeys.DARK_MODE)?.toBoolean() ?: false
    
    suspend fun setDarkModeEnabled(enabled: Boolean) = 
        setSetting(SettingsKeys.DARK_MODE, enabled.toString())
    
    suspend fun getFireSWR(): Double = 
        getSettingValue(SettingsKeys.FIRE_SWR)?.toDoubleOrNull() ?: 4.0
    
    suspend fun setFireSWR(swr: Double) = setSetting(SettingsKeys.FIRE_SWR, swr.toString())
}
