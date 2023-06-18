package com.example.todomanagement.database

import androidx.lifecycle.LiveData
import com.example.todomanagement.util.Converter
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskRepository(private val database: TaskRoomDatabase) {

    suspend fun insertSingleTask(task: Task) {
        withContext(Dispatchers.IO) {
            database.taskDao.insertTask(task)
        }
    }

    /**
     * 保存单个任务，有冲突就合并
     */
    suspend fun saveTask(task: Task) {
        coroutineScope {
            launch {
                database.taskDao.insertTask(task)
            }
        }
    }

    /**
     * 将单个task所属的分类保存到category数据库
     */
    suspend fun saveCategory(category: Category) {
        coroutineScope {
            launch {
                database.categoryDao.insertCategory(category)
            }
        }
    }

    /**
     * 观察所有task的数据
     *
     * @return 返回所有live data型数据
     */
    fun observeTasks(): LiveData<List<Task>> {
        return database.taskDao.observeTasks()
    }

    /**
     * 观察今天的task，对task进行筛选并提取
     */
    fun getTodayTask(): LiveData<List<Task>> {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val tomorrow = Converter.getTomorrowInMillSec(today)
        return database.taskDao.observeTaskInSection(today, tomorrow)
    }

    fun getCategories(): LiveData<List<Category>> {
        return database.categoryDao.observeCategory()
    }

    /**
     * 观察单个给定id的task
     *
     * @return LiveData<Task>型数据
     */
    fun observeTaskById(taskId: String): LiveData<Task> {
        return database.taskDao.observeTaskById(taskId)
    }

    /**
     * 返回单个task的数据
     */
    suspend fun getTaskById(taskId: String): Task? {
        return database.taskDao.getTaskById(taskId)
    }

    /**
     * 更新整个任务
     */
    suspend fun updateTask(task: Task) {
        coroutineScope {
            launch {
                database.taskDao.updateTask(task)
            }
        }
    }

    /**
     * 将任务标记为完成了
     */
    suspend fun taskMarkedCompleted(taskId: String) {
        coroutineScope {
            launch {
                database.taskDao.updateCompleted(taskId, true)
            }
        }
    }

    /**
     * 将任务标记为未完成
     */
    suspend fun taskMarkedPending(taskId: String) {
        coroutineScope {
            launch {
                database.taskDao.updateCompleted(taskId, false)
            }
        }
    }

    /**
     * 根据id删除任务
     */
    suspend fun deleteTaskById(taskId: String) {
        coroutineScope {
            launch {
                database.taskDao.deleteTaskById(taskId)
            }
        }
    }

    /**
     * 根据id删除category
     */
    suspend fun deleteCategoryById(taskId: Long) {
        coroutineScope {
            launch {
                database.categoryDao.deleteCategoryById(taskId)
            }
        }
    }
}