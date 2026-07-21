package com.onear.doplusplus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.onear.doplusplus.data.entity.TodoTask
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {
    //增
    @Insert
    suspend fun insertTask(task: TodoTask)

    //删
    @Delete
    suspend fun deleteTask(task: TodoTask)

    //改
    @Update
    suspend fun updateTask(task: TodoTask)

    @Query("SELECT * FROM todo_database ORDER BY taskCreateDate DESC")
    fun getAllTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_database WHERE filterTag = :tag ORDER BY taskCreateDate DESC")
    fun getTasksByTag(tag: String): Flow<List<TodoTask>>
}