package com.example.todomanagement.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    /**
     * 观察所有的task，随时更改,按新建时间先后排序
     *
     * @return 所有task
     */
    @Query("SELECT * FROM Tasks ORDER BY start_time_milli DESC")
    fun observeTasks(): LiveData<List<Task>>

    /**
     * 通过起始时间观察任务
     *
     * @return 返回所有在这个时间范围的列表
     */
    @Query("SELECT * FROM Tasks WHERE end_time_milli BETWEEN :startTimestamp AND :endTimeStamp ORDER BY end_time_milli ASC")
    fun observeTaskInSection(startTimestamp: Long, endTimeStamp: Long): LiveData<List<Task>>

    /**
     * 通过id观察单个任务
     *
     * @param taskId 任务的id
     * @return live data task对象
     */
    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    fun observeTaskById(taskId: String): LiveData<Task>

    /**
     * 选中所有的task,注意，该task都是根据添加时间排序
     *
     * @return 返回非live data的格式 tasks.
     */
    @Query("SELECT * FROM Tasks ORDER BY start_time_milli DESC")
    suspend fun getTasks(): List<Task>

    /**
     * 选中单个task
     *
     * @param taskId task 的选中id
     * @return
     */
    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    /**
     * 在数据库中插入一个任务。如果任务已经存在，请替换该任务。
     *
     * @param task 插入的task
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /**
     * 更新task
     *
     * @param task 要更新的task
     * @return 更新的任务数。这个应该总是1。
     */
    @Update
    suspend fun updateTask(task: Task): Int

    /**
     * 改变任务的完成状态
     *
     * @param taskId    待更新的task
     * @param completed 待更新的状态
     */
    @Query("UPDATE tasks SET completed = :completed WHERE entryid = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    /**
     * 根据id删除任务
     *
     * @return 删除的任务数量.应该总是1
     */
    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    /**
     * 清空全部task
     */
    @Query("DELETE FROM Tasks")
    suspend fun deleteTasks()

    /**
     * 清空所有已完成的任务
     *
     * @return 删除的任务数量
     */
    @Query("DELETE FROM Tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int
}