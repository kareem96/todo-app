package com.example.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.todoapp.utils.FilterUtils
import com.example.todoapp.utils.TaskFilterType

class TaskRepo(private val taskDao: TaskDao) {
    fun getTask(filter: TaskFilterType): LiveData<PagedList<Task>> {
        val query = FilterUtils.getFilteredQuery(filter)
        return taskDao.getTask(query).toLiveData(Config(pageSize = PAGE_SIZE))
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }

    fun getActiveTask(): Task {
        return taskDao.getActiveTask()
    }

    suspend fun insertTask(newTask: Task): Long {
        return taskDao.insertTask(newTask)
    }

    suspend fun completeTask(task: Task, isComplete: Boolean) {
        taskDao.updateComplete(task.id, isComplete)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepo? = null
        fun getInstance(context: Context): TaskRepo {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDb.getInstance(context)
                    instance = TaskRepo(database.taskDao())
                }
                return instance as TaskRepo
            }
        }
    }
}