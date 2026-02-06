package com.example.expensetracker.data.repository

import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    fun getCategoriesByType(type: String): Flow<List<Category>> = categoryDao.getCategoriesByType(type)
    
    fun getParentCategories(): Flow<List<Category>> = categoryDao.getParentCategories()
    
    fun getSubCategories(parentId: Long): Flow<List<Category>> = categoryDao.getSubCategories(parentId)
    
    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)
    
    suspend fun getAllCategoriesSync(): List<Category> = categoryDao.getAllCategoriesSync()
    
    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    
    suspend fun archiveCategory(id: Long) = categoryDao.archiveCategory(id)
    
    suspend fun updateDisplayOrder(id: Long, order: Int) = categoryDao.updateDisplayOrder(id, order)
}
