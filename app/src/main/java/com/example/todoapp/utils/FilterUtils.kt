package com.example.todoapp.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

enum class TaskFilterType {
    ALL_TASK,
    ACTIVE_TASK,
    COMPLETED_TASK,
}

object FilterUtils {
    fun getFilteredQuery(filter: TaskFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("select * from tasks ")
        when (filter) {
            TaskFilterType.COMPLETED_TASK -> {
                simpleQuery.append("WHERE completed = 1")
            }

            TaskFilterType.ACTIVE_TASK -> {
                simpleQuery.append("WHERE completed = 0")
            }

            else -> {
                TaskFilterType.ALL_TASK
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}