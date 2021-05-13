package com.app.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.todo.model.Task
import java.util.*

/**
 * Interface provides abstract access to app's database.
 */
@Dao
interface TaskDao {
    /**
     * Add task to a database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    /**
     * Read all tasks as LiveData from a database.
     */
    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun readAllData(): LiveData<List<Task>>

    /**
     * Read all tasks as List from a database.
     */
    @Query("SELECT * FROM task_table")
    fun readTasks(): List<Task>

    /**
     * Updates task in database.
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Remove single task in database.
     */
    @Delete
    suspend fun deleteTask(task: Task)


    /**
     * Remove all tasks in database.
     */
    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    /**
     * Read all tasks with specified priority.
     */
    @Query("SELECT * FROM task_table WHERE priority = :priority")
    fun filterByPriority(priority: String): LiveData<List<Task>>


    /**
     * Read tasks with specified priority sorted by name.
     */
    @Query("SELECT * FROM task_table WHERE priority = :priority ORDER BY name ASC")
    fun filterBySortedByName(priority: String): LiveData<List<Task>>


    /**
     * Read tasks with specified priority sorted by name.
     */
    @Query("SELECT * FROM task_table WHERE priority = :priority ORDER BY type ASC")
    fun filterBySortedByType(priority: String): LiveData<List<Task>>


    /**
     * Read task with specified priority sorted by date.
     */
    @Query("SELECT * FROM task_table WHERE priority = :priority ORDER BY date ASC")
    fun filterBySortedByDate(priority: String): LiveData<List<Task>>


    /**
     * Read all tasks sorted by name.
     */
    @Query("SELECT * FROM task_table ORDER BY name ASC")
    fun sortedByName(): LiveData<List<Task>>

    /**
     * Read all tasks sorted by type.
     */
    @Query("SELECT * FROM task_table ORDER BY type ASC")
    fun sortedByType(): LiveData<List<Task>>

    /**
     * Read all tasks sorted by year.
     */
    @Query("SELECT * FROM task_table ORDER BY date ASC")
    fun sortedByDate(): LiveData<List<Task>>

    /**
     * Read all tasks sorted by priority.
     */
    @Query("SELECT * FROM task_table ORDER BY priority ASC")
    fun sortedByPriority(): LiveData<List<Task>>

    /**
     * Read all tasks sorted by name.
     */
    @Query("SELECT * FROM task_table WHERE date > :date1 AND date < :date2")
    fun getTasksFrom(date1: Calendar, date2: Calendar): LiveData<List<Task>>

}