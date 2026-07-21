package com.onear.doplusplus.data

import com.onear.doplusplus.data.entity.FilterTag
import com.onear.doplusplus.data.entity.TodoTask
import com.onear.doplusplus.data.local.FilterDao
import com.onear.doplusplus.data.local.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val todoDao: TodoDao,
    private val filterDao: FilterDao
) {
    val allTasks: Flow<List<TodoTask>> = todoDao.getAllTasks()

    fun getTasksByTag(tag: String): Flow<List<TodoTask>> = todoDao.getTasksByTag(tag)

    suspend fun addTask(task: TodoTask) {
        todoDao.insertTask(task)
    }

    suspend fun updateTask(task: TodoTask) {
        todoDao.updateTask(task)
    }

    suspend fun deleteTask(task: TodoTask) {
        todoDao.deleteTask(task)
    }

    val allFilters: Flow<List<FilterTag>> = filterDao.getAllFilters()

    suspend fun addFilter(filter: FilterTag) {
        filterDao.insert(filter)
    }

    suspend fun deleteFilter(filter: FilterTag) {
        filterDao.delete(filter)
    }

    suspend fun updateFilter(filter: FilterTag) {
        filterDao.update(filter)
    }
}