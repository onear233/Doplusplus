package com.onear.doplusplus.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onear.doplusplus.R
import com.onear.doplusplus.ui.data.TodoRepository
import com.onear.doplusplus.ui.data.entity.TodoTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun completeTask(todoTask: TodoTask,isChecked : Boolean) {
        viewModelScope.launch {
            // 💡 利用 copy 创建一个全新对象，并把状态反转
            val updatedTask = todoTask.copy(isCompleted = !todoTask.isCompleted)
            // 💡 写入数据库。Room 写入成功后，Flow 会自动发射新列表，UI 就会瞬间刷新！
            todoRepository.updateTask(updatedTask)
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