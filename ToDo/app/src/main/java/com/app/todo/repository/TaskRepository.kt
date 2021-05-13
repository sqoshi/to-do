package com.app.todo.repository

import androidx.lifecycle.LiveData
import com.app.todo.Auxiliary
import com.app.todo.data.TaskDao
import com.app.todo.model.Task
import java.util.*

/**
 * Access to multiple data sourced. Handles data operations.
 */
class TaskRepository(private val taskDao: TaskDao) {
    val readAllData: LiveData<List<Task>> = taskDao.readAllData()
    val sortedByName: LiveData<List<Task>> = taskDao.sortedByName()
    val sortedByType: LiveData<List<Task>> = taskDao.sortedByType()
    val sortedByDate: LiveData<List<Task>> = taskDao.sortedByDate()
    val sortedByPriority: LiveData<List<Task>> = taskDao.sortedByPriority()


    fun filterByPrioritySortedBy(
        priority: String,
        sortType: Auxiliary.Companion.SortType
    ): LiveData<List<Task>> {
        return when (sortType) {
            Auxiliary.Companion.SortType.DATE -> taskDao.filterBySortedByDate(priority)
            Auxiliary.Companion.SortType.NAME -> taskDao.filterBySortedByName(priority)
            Auxiliary.Companion.SortType.TYPE -> taskDao.filterBySortedByType(priority)
        }
    }

    /**
     * Return live data list filtered by priority.
     */
    fun filterByPriority(priority: String): LiveData<List<Task>> {
        return taskDao.filterByPriority(priority)
    }

    /**
     * Return tasks from specified date.
     */
    fun getTasksFrom(date: Calendar): LiveData<List<Task>> {
        val date2 = Auxiliary.getCalendarWithDate(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE)
        )
        date2.add(Calendar.DATE, 1)
        return taskDao.getTasksFrom(date, date2)
    }

    /**
     * Return list of tasks.
     */
    fun readTasks(): List<Task> {
        return taskDao.readTasks()
    }

    /**
     * Add task to database.
     */
    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    /**
     * Update task in database.
     */
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    /**
     * Remove task from database.
     */
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    /**
     * Remove all tasks from database.
     */
    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

}