package com.example.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.concurrent.Executors

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDb : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDb? = null
        fun getInstance(context: Context): TaskDb {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context.applicationContext, TaskDb::class.java, "task.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                Executors.newSingleThreadScheduledExecutor().execute {
                                    fillWithStartingData(context, getInstance(context).taskDao())
                                }
                            }
                        }).build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, taskDao: TaskDao) {
            val task = loadJsonArray(context)
            try {
                if (task != null) {
                    for (i in 0 until task.length()) {
                        val item = task.getJSONObject(i)
                        taskDao.insertAllTask(
                            Task(
                                item.getInt("id"),
                                item.getString("title"),
                                item.getString("description"),
                                item.getLong("dueDate"),
                                item.getBoolean("completed"),
                            )
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.task)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("tasks")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }
    }
}