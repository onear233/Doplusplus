package com.onear.doplusplus.viewmodel

import android.icu.util.Calendar
import androidx.compose.ui.res.stringResource
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

class TodayViewModel() : ViewModel() {
    //使用StateFlow存储当前的问候语
    private val _greeting = MutableStateFlow(getGreetingMessage())
    val greeting: StateFlow<Int> = _greeting.asStateFlow()
    fun updateGreeting() {
        _greeting.value = getGreetingMessage()
    }

    private fun getGreetingMessage(): Int {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..10 -> R.string.greeting_morning
            in 11..13 -> R.string.greeting_noon
            in 13..17 -> R.string.greeting_afternoon
            else -> {
                R.string.greeting_night
            }
        }
    }
}