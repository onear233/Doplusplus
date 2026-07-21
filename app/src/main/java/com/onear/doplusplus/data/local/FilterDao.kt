package com.onear.doplusplus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.onear.doplusplus.data.entity.FilterTag
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {
    @Insert
    suspend fun insert(filter: FilterTag)

    @Delete
    suspend fun delete(filter: FilterTag)

    @Update
    suspend fun update(filter: FilterTag)

    @Query("SELECT * FROM filter_tags ORDER BY id ASC")
    fun getAllFilters(): Flow<List<FilterTag>>
}
