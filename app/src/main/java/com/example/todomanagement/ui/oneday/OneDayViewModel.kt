package com.example.todomanagement.ui.oneday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todomanagement.database.Task
import com.example.todomanagement.database.TaskRepository
import com.example.todomanagement.database.TaskRoomDatabase

class OneDayViewModel(application: Application) : AndroidViewModel(application) {
    //获取数据库
    private val tasksRepository = TaskRepository(TaskRoomDatabase.getInstance(application))

    //我的一天的数据集
    private val _items = tasksRepository.getTodayTask()
    val items: LiveData<List<Task>> = _items
}