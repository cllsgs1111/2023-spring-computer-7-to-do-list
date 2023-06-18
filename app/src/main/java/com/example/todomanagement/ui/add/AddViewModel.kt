package com.example.todomanagement.ui.add

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

class AddViewModel(private val app: Application) : AndroidViewModel(app) {
    private val REQUEST_CODE = 0

    private val tasksRepository: TaskRepository =
            TaskRepository(TaskRoomDatabase.getInstance(app))

    //alarm manager来调用pending intent计时
    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //TODO 从这里启动alarm receiver服务，用来构建pending intent
    private val notifyIntent = Intent(app, AlarmReceiver::class.java)

    // 绑定title
    val title = MutableLiveData<String>()

    // 绑定内容
    val description = MutableLiveData<String>()

    //时间辅助类，使用livedata响应用户更改
    var time = MutableLiveData<DateTime>()

    //
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    //snackbar显示内容
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    //更新内容类
    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    init {
        time.value = DateTime()
    }

    /**
     * 保存任务并设定闹钟，到时间就推送
     */
    fun saveTask(view: View) {
        val currentTitle = title.value
        val currentDescription = description.value
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
        notifyIntent.putExtra("TITLE", title.value)
        notifyIntent.putExtra("DESCRIPTION", description.value)
        //启动计时任务
        startTimer(interval)
        //没问题了，创建任务,防止currentTime为空
        createTask(Task(currentTitle, currentDescription, currentDateTime))
        Navigation.findNavController(view).navigate(R.id.action_addFragment_to_navigation_all)
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