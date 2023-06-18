package com.example.todomanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class, Category::class], version = 6, exportSchema = false)
abstract class TaskRoomDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao

    companion object {
        //volatile注释不允许缓存该变量
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getInstance(context: Context): TaskRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                // TODO 如果数据库为空，那么就新建一个数据库实例，不允许数据库转移储存
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            TaskRoomDatabase::class.java,
                            "task_database")
                            .fallbackToDestructiveMigration()
                            .build()
                    // 将INSTANCE分配给新创建的数据库。
                    INSTANCE = instance
                }

                // 返回一个数据库实例
                return instance
            }
        }
    }
}