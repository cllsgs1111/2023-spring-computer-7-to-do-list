package com.example.todomanagement.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    /**
     * 获取所有category数据
     */
    @Query("SELECT * FROM CATEGORY ORDER BY id ASC")
    fun observeCategory(): LiveData<List<Category>>

    /**
     * 添加新的category
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    /**
     * 根据id删除category
     *
     * @return 删除的category数量.应该总是1
     */
    @Query("DELETE FROM CATEGORY WHERE id = :taskId")
    suspend fun deleteCategoryById(taskId: Long): Int
}