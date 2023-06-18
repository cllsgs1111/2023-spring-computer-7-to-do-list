package com.example.todomanagement.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todomanagement.database.Category
import com.example.todomanagement.database.TaskRepository
import com.example.todomanagement.database.TaskRoomDatabase
import com.example.todomanagement.util.Event
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {
    private val tasksRepository: TaskRepository =
            TaskRepository(TaskRoomDatabase.getInstance(application))

    //开启任务时，监测task更改，当点击时就转到add fragment
    private val _openOverviewEvent = MutableLiveData<Event<String>>()
    val openOverviewEvent: LiveData<Event<String>> = _openOverviewEvent

    //TODO 先列出各个分类，呈现各个分类的数据集
    private val _items = tasksRepository.getCategories()
    val items: LiveData<List<Category>> = _items

    fun addCategory(content: String) {
        viewModelScope.launch {
            tasksRepository.saveCategory(Category(content))
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            tasksRepository.deleteCategoryById(id)
        }
    }

    /**
     * 打开编辑task的界面
     */
    fun openOverview(categoryId: Long) {
        _openOverviewEvent.value = Event(categoryId.toString())
    }
}