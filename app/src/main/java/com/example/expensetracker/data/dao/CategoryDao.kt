package com.example.expensetracker.data.dao

import androidx.room.*
import com.example.expensetracker.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE isArchived = 0 ORDER BY displayOrder ASC, name ASC")
    fun getAllCategories(): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE type = :type AND isArchived = 0 ORDER BY displayOrder ASC, name ASC")
    fun getCategoriesByType(type: String): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE parentCategoryId IS NULL AND isArchived = 0 ORDER BY displayOrder ASC, name ASC")
    fun getParentCategories(): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE parentCategoryId = :parentId AND isArchived = 0 ORDER BY displayOrder ASC, name ASC")
    fun getSubCategories(parentId: Long): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?
    
    @Query("SELECT * FROM categories WHERE isArchived = 0")
    suspend fun getAllCategoriesSync(): List<Category>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long
    
    @Update
    suspend fun updateCategory(category: Category)
    
    @Delete
    suspend fun deleteCategory(category: Category)
    
    @Query("UPDATE categories SET isArchived = 1 WHERE id = :id")
    suspend fun archiveCategory(id: Long)
    
    @Query("UPDATE categories SET displayOrder = :order WHERE id = :id")
    suspend fun updateDisplayOrder(id: Long, order: Int)
}
