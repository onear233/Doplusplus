package com.onear.doplusplus.data

import com.onear.doplusplus.data.entity.TodoTask
import com.onear.doplusplus.data.local.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTasks: Flow<List<TodoTask>> = todoDao.getAllTasks()

//    suspend fun addTask(title: String) {
//        if (title.isNotBlank()) {
//            todoDao.insertTask(
//                //MOCK DATA
//                TodoTask(
//                    taskID = 0,
//                    taskText = title,
//                    taskDueDate = null,
//                    isCompleted = false
//                )
//            )
//        }
//    }

    suspend fun toggleTaskCompletion(task: TodoTask) {
        todoDao.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(task: TodoTask) {
        todoDao.deleteTask(task)
    }

    suspend fun addTask(task: TodoTask){
        todoDao.insertTask(task)
    }
    suspend fun updateTask(task: TodoTask){
        todoDao.updateTask(task)
    }
}