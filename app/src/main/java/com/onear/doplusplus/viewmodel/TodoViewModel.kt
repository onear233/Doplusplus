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


    //TODO("UNDERSTANDING THE CODE DEEPLY")
    val todoListState: StateFlow<List<TodoTask>> = todoRepository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


}