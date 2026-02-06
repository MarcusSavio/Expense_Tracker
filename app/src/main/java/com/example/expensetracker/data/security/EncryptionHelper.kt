package com.example.expensetracker.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

/**
 * Simple encryption helper for v1
 * Uses Android's EncryptedSharedPreferences for settings
 * Database files are protected by Android's file system permissions (private to app)
 * Sensitive data can be encrypted at field level if needed
 */
object EncryptionHelper {
    private const val PREFS_NAME = "expense_tracker_encrypted_prefs"
    private const val MASTER_KEY_ALIAS = "expense_tracker_master_key"
    
    /**
     * Get encrypted shared preferences for sensitive settings
     */
    fun getEncryptedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    /**
     * Simple AES encryption for sensitive strings (if needed)
     * For v1, database files are protected by Android's file system permissions
     */
    fun encryptString(value: String, key: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(value.toByteArray())
        val iv = cipher.iv
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }
    
    /**
     * Simple AES decryption
     */
    fun decryptString(encrypted: String, key: SecretKey): String {
        val decoded = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = ByteArray(12) // GCM IV size
        System.arraycopy(decoded, 0, iv, 0, 12)
        val ciphertext = ByteArray(decoded.size - 12)
        System.arraycopy(decoded, 12, ciphertext, 0, ciphertext.size)
        
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return String(cipher.doFinal(ciphertext))
    }
}
