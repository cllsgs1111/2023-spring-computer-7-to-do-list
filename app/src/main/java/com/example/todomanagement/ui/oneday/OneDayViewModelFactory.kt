package com.example.todomanagement.ui.oneday

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OneDayViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OneDayViewModel::class.java)) {
            return OneDayViewModel(application) as T
        } else {
            throw IllegalArgumentException("未知的view Model类，可能传入了错误的view model")
        }
    }

}