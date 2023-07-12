package com.example.todoapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepo
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: TaskRepo) : ViewModel() {
    private val _taskId = MutableLiveData<Int>()

    private val _task = _taskId.switchMap { id ->
        repository.getTaskById(id)
    }

    val task: LiveData<Task> = _task
    fun setTask(taskId: Int) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
    }

    fun deleteTsk() {
        viewModelScope.launch {
            _task.value?.let { repository.deleteTask(it) }
        }
    }
}