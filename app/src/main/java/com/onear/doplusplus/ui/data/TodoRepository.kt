package com.onear.doplusplus.ui.data

import com.onear.doplusplus.ui.data.entity.TodoTask
import com.onear.doplusplus.ui.data.local.TodoDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TodoRepository(private val todoDao: TodoDao) {
    val allTasks: Flow<List<TodoTask>> = todoDao.getAllTasks()

    suspend fun addTask(title: String) {
        if (title.isNotBlank()) {
            todoDao.insertTask(
                //MOCK DATA
                TodoTask(
                    taskID = 0,
                    taskText = "TODO()",
                    taskDueDate = null,
                    taskCreateDate = 114514,
                    isCompleted = false
                )
            )
        }
    }

    suspend fun toggleTaskCompletion(task: TodoTask) {
        todoDao.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(task: TodoTask) {
        todoDao.deleteTask(task)
    }
}