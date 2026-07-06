package com.onear.doplusplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onear.doplusplus.data.TodoRepository
import com.onear.doplusplus.data.entity.TodoTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    fun addTask(title: String) {
        viewModelScope.launch {
            todoRepository.addTask(
                TodoTask(
                    taskText = title,
                    taskDueDate = null,
                )
            )
        }

    }

    fun updateTask(todoTask: TodoTask){
        viewModelScope.launch { todoRepository.updateTask(todoTask) }
    }
    fun completeTask(todoTask: TodoTask) {
        viewModelScope.launch {
            //利用 copy 创建一个全新对象，并把状态反转
            val updatedTask = todoTask.copy(isCompleted = !todoTask.isCompleted)
            //写入数据库，Room 写入成功后，Flow会自动发射新列表，UI刷新
            todoRepository.updateTask(updatedTask)
        }
    }

    fun deleteTask(todoTask: TodoTask) {
        viewModelScope.launch {
            todoRepository.deleteTask(todoTask)
        }
    }


    //TODO("UNDERSTANDING THE CODE DEEPLY")
    val todoListState: StateFlow<List<TodoTask>> = todoRepository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


}