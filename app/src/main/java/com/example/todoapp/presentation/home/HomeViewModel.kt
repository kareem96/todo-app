package com.example.todoapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepo
import com.example.todoapp.utils.Event
import com.example.todoapp.utils.TaskFilterType
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TaskRepo) : ViewModel() {
    private val _filter = MutableLiveData<TaskFilterType>()
    private val _snackBar = MutableLiveData<Event<Int>>()
    val snackBar: LiveData<Event<Int>> = _snackBar

    init {
        _filter.value = TaskFilterType.ALL_TASK
    }

    val task: LiveData<PagedList<Task>> = _filter.switchMap {
        repository.getTask(it)
    }

    fun filter(filterType: TaskFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, complete: Boolean) = viewModelScope.launch {
        repository.completeTask(task, complete)
        if (complete) {
            _snackBar.value = Event(R.string.task_marked_complete)
        } else _snackBar.value = Event(R.string.task_marked_active)
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

}