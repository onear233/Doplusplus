package com.onear.doplusplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onear.doplusplus.data.TodoRepository
import com.onear.doplusplus.data.entity.FilterTag
import com.onear.doplusplus.data.entity.TodoTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    private val selectedFilter: MutableStateFlow<String?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val todoListState: StateFlow<List<TodoTask>> = selectedFilter
        .flatMapLatest { tag ->
            if (tag == null) todoRepository.allTasks
            else todoRepository.getTasksByTag(tag)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filterListState: StateFlow<List<FilterTag>> = todoRepository.allFilters
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val selectedFilterState: StateFlow<String?> = selectedFilter

    fun selectFilter(tag: String?) {
        selectedFilter.value = tag
    }

    fun addTask(title: String, dueDate: Long? = null, filterTag: String? = null) {
        viewModelScope.launch {
            todoRepository.addTask(
                TodoTask(
                    taskText = title,
                    taskDueDate = dueDate,
                    filterTag = filterTag
                )
            )
        }
    }

    fun updateTask(todoTask: TodoTask) {
        viewModelScope.launch { todoRepository.updateTask(todoTask) }
    }

    fun completeTask(todoTask: TodoTask) {
        viewModelScope.launch {
            val updatedTask = todoTask.copy(isCompleted = !todoTask.isCompleted)
            todoRepository.updateTask(updatedTask)
        }
    }

    fun deleteTask(todoTask: TodoTask) {
        viewModelScope.launch { todoRepository.deleteTask(todoTask) }
    }

    fun addFilter(name: String, color: Long = 0xFF6750A4) {
        viewModelScope.launch {
            todoRepository.addFilter(FilterTag(name = name, color = color))
        }
    }

    fun deleteFilter(filter: FilterTag) {
        viewModelScope.launch {
            if (selectedFilter.value == filter.name) {
                selectedFilter.value = null
            }
            todoRepository.deleteFilter(filter)
        }
    }
}