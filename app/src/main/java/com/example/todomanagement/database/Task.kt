package com.example.todomanagement.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
        @ColumnInfo(name = "title")
        var title: String = "",

        @ColumnInfo(name = "description")
        var description: String = "",

        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli: Long? = System.currentTimeMillis(),

        @ColumnInfo(name = "start_time_milli")
        val startTimeMilli: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "completed")
        var isCompleted: Boolean = false,

        @ColumnInfo(name = "category")
        var category: String = "默认分类",

        @PrimaryKey
        @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {
    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}