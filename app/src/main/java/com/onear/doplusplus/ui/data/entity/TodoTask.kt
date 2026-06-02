package com.onear.doplusplus.ui.data.entity

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

/*
    表示todolist中的一条
 */

@Entity(tableName = "todo_database")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val taskID: Int,
    var taskText: String,
    var taskDueDate: Long?,
    val taskCreateDate: Long,
    var isCompleted: Boolean = false
)