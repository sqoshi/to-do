package com.app.todo.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.*
import com.app.todo.Auxiliary
import com.app.todo.data.TaskDatabase
import com.app.todo.model.Task
import com.app.todo.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


/**
 * Its role is to provide data to UI and survive configuration changes.
 */
class TaskViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    val readAllData: LiveData<List<Task>>
    val sortedByName: LiveData<List<Task>>
    val sortedByType: LiveData<List<Task>>
    val sortedByDate: LiveData<List<Task>>
    val sortedByPriority: LiveData<List<Task>>
    private val repository: TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        readAllData = repository.readAllData
        sortedByName = repository.sortedByName
        sortedByType = repository.sortedByType
        sortedByDate = repository.sortedByDate
        sortedByPriority = repository.sortedByPriority


    }


    /**
     * Saves state for example on orientation change.
     */
    fun saveState() {
        state.set("readAllData", readAllData.value)
    }


    /**
     * Filter elements by priority and sorts by sort type passed as arg.
     */
    fun filterByPrioritySortedBy(
        priority: String,
        sortType: Auxiliary.Companion.SortType
    ): LiveData<List<Task>> {
        return repository.filterByPrioritySortedBy(priority, sortType)
    }

    /**
     * Filter elements by priority passed as arg.
     */
    fun filterByPriority(priority: String): LiveData<List<Task>> {
        return repository.filterByPriority(priority)
    }

    /**
     * Found task from specified date.
     */
    fun getTasksFrom(date: Calendar): LiveData<List<Task>> {
        return repository.getTasksFrom(date)
    }

    /**
     * Add task object to database.
     */
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    /**
     * Updates element by passing a task (with concrete id).
     */
    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    /**
     * Removes element from database.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    /**
     * Remove all tasks from database
     */
    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    /**
     * Using async reading all tasks as list.
     */
    fun readTasks(): List<Task>? {
        return AsyncGet().execute().get()
    }

    /**
     * Allow to asynchronous request to database.
     */
    @SuppressLint("StaticFieldLeak")
    inner class AsyncGet : AsyncTask<Void, Void, List<Task>>() {
        override fun doInBackground(vararg params: Void?): List<Task> {
            return repository.readTasks()
        }

    }


}