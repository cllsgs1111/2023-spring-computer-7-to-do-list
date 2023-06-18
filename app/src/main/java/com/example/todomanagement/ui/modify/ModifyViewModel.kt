package com.example.todomanagement.ui.modify

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.todomanagement.R
import com.example.todomanagement.database.Task
import com.example.todomanagement.database.TaskRepository
import com.example.todomanagement.database.TaskRoomDatabase
import com.example.todomanagement.receiver.AlarmReceiver
import com.example.todomanagement.util.Converter
import com.example.todomanagement.util.DateTime
import com.example.todomanagement.util.Event
import kotlinx.coroutines.launch

class ModifyViewModel(application: Application, taskId: String) : AndroidViewModel(application) {
    private val REQUEST_CODE = 0

    private val tasksRepository: TaskRepository =
            TaskRepository(TaskRoomDatabase.getInstance(application))

    //alarm manager来调用pending intent计时
    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //TODO 从这里启动alarm receiver服务，用来构建pending intent
    private val notifyIntent = Intent(application, AlarmReceiver::class.java)

    //获取当前编辑的task对象
    val task = MutableLiveData<Task>()

    //时间辅助类，使用livedata响应用户更改
    val time = MutableLiveData<DateTime>()

    val timeString = Transformations.map(task) {
        return@map Converter.formatDateTimeString(task.value?.endTimeMilli)
    }

    //snackbar显示内容
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    //更新内容类
    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    init {
        viewModelScope.launch {
            task.value = tasksRepository.getTaskById(taskId)

            //将保存了的秒数转化为time和date
            if (task.value != null) {
                time.value = Converter.convertMillSecToDateTime(task.value!!.endTimeMilli)
            } else {
                time.value = DateTime()
            }
        }
    }

    /**
     * 保存任务并设定闹钟，到时间就推送
     */
    fun saveTask(view: View) {
        val currentTitle = task.value?.title
        val currentDescription = task.value?.description
        val currentDateTime =
                time.value?.let {
                    Converter.convertDateTimeToMillSec(it.date, it.hour, it.minute)
                }
        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if (Task(currentTitle, currentDescription).isEmpty) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        val interval: Long = currentDateTime!! - System.currentTimeMillis()

        if (interval < 0) {
            _snackbarText.value = Event(R.string.error_time_message)
            return
        }

        //向intent传递参数
        notifyIntent.putExtra("TITLE", task.value?.title)
        notifyIntent.putExtra("DESCRIPTION", task.value?.description)
        //启动计时任务
        startTimer(interval)

        //没问题了，更新数据
        if (task.value == null) {
            createTask(Task(currentTitle, currentDescription, currentDateTime))

        } else {
            task.value!!.endTimeMilli = currentDateTime
            updateTask(task.value!!)
        }
        Navigation.findNavController(view).navigate(R.id.action_modifyFragment_to_navigation_all)
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.updateTask(task)
            _taskUpdatedEvent.value = Event(Unit)
        }
        _snackbarText.value = Event(R.string.task_modified_succeed)
    }

    private fun createTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.saveTask(task)
            _taskUpdatedEvent.value = Event(Unit)
        }
        _snackbarText.value = Event(R.string.task_added_succeed)
    }

    private fun startTimer(interval: Long) {
        //TODO SystemClock.elapsedRealtime() 是设备从开机到现在经历的时间
        val triggerTime = SystemClock.elapsedRealtime() + interval

        //TODO 构建pending intent
        val notifyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                getApplication(),
                REQUEST_CODE,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        //TODO 启动推送的pending intent
        AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                notifyPendingIntent
        )
    }
}