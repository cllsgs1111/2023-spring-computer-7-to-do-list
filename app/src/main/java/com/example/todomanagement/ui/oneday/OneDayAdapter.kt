package com.example.todomanagement.ui.oneday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanagement.database.Task
import com.example.todomanagement.databinding.ItemOnedayBinding

class OneDayAdapter(private val viewModel: OneDayViewModel) :
    ListAdapter<Task, OneDayAdapter.ViewHolder>(OneDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder constructor(val binding: ItemOnedayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: OneDayViewModel, item: Task) {
            binding.viewmodel = viewModel
            binding.task = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOnedayBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}