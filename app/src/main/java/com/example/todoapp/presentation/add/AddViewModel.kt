package com.example.todoapp.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepo
import kotlinx.coroutines.launch

class AddViewModel(private val repository: TaskRepo) : ViewModel() {
    fun addTask(newsTask: Task) = viewModelScope.launch { repository.insertTask(newsTask) }
}