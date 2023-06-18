package com.example.todomanagement.ui.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todomanagement.R
import com.example.todomanagement.database.Task
import com.example.todomanagement.database.TaskRepository
import com.example.todomanagement.database.TaskRoomDatabase
import com.example.todomanagement.util.Event
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application) : AndroidViewModel(application) {
    //获取数据库
    private val tasksRepository = TaskRepository(TaskRoomDatabase.getInstance(application))

    //开启任务时，监测task更改，当点击时就转到add fragment
    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    //snackbar显示的信息，livedata处理
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    //
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _items = tasksRepository.observeTasks()
    val items: LiveData<List<Task>> = _items

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    /*fun showEditResultMessage() {
        showSnackbarMessage(R.string.successfully_saved_task_message)
    }*/

    /**
     * 打开编辑task的界面
     */
    fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    /**
     * 当checkbox 选中完成任务时
     */
    fun completeTask(taskId: String, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            tasksRepository.taskMarkedCompleted(taskId)
            showSnackbarMessage(R.string.task_marked_complete)
            tasksRepository.deleteTaskById(taskId)
        } else {
            tasksRepository.taskMarkedPending(taskId)
            showSnackbarMessage(R.string.task_modified_succeed)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            tasksRepository.deleteTaskById(taskId)
        }
    }
}