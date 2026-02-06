package com.example.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val parentCategoryId: Long? = null, // For subcategories
    val iconName: String? = null, // Icon identifier
    val color: String? = null, // Hex color code
    val description: String? = null,
    val isArchived: Boolean = false,
    val displayOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CategoryType {
    EXPENSE,
    INCOME,
    BOTH
}
