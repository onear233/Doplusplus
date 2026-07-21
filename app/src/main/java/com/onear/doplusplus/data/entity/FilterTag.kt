package com.onear.doplusplus.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter_tags")
data class FilterTag(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val color: Long = 0xFF6750A4
)
