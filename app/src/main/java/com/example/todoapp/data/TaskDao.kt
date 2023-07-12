package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface TaskDao {
    @RawQuery(observedEntities = [Task::class])
    fun getTask(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>

    @Query("select * from tasks where id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("select * from tasks where not completed order by date ASC")
    fun getActiveTask(): Task

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTask(vararg task: Task)

    @Query("update tasks set completed = :completed where id = :taskId")
    suspend fun updateComplete(taskId: Int, completed: Boolean)

    @Delete
    suspend fun deleteTask(task: Task)

}