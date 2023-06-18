package com.example.todomanagement.ui.tasklist

import androidx.recyclerview.widget.DiffUtil
import com.example.todomanagement.database.Category

class TaskListDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}