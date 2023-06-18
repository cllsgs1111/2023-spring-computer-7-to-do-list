package com.example.todomanagement.ui.modify

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ModifyViewModelFactory(private val application: Application, private val taskId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyViewModel::class.java)) {
            return ModifyViewModel(application, taskId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}