package com.example.todomanagement.ui.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanagement.database.Category
import com.example.todomanagement.databinding.ItemItemlistBinding

class TaskListAdapter(private val viewModel: TaskListViewModel) :
        ListAdapter<Category, TaskListAdapter.ViewHolder>(TaskListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder constructor(val binding: ItemItemlistBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TaskListViewModel, item: Category) {
            binding.viewmodel = viewModel
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemItemlistBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}