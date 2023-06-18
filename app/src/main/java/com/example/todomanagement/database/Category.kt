package com.example.todomanagement.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category @JvmOverloads constructor(
        @ColumnInfo(name = "name")
        var title: String = "默认分类",

        @PrimaryKey
        var id: Long = System.currentTimeMillis()
)
