package com.example.todoapp.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepo
import com.example.todoapp.presentation.detail.DetailActivity
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.example.todoapp.utils.TASK_ID

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)
    private val repository = TaskRepo.getInstance(context)
    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {
        val activeTask = repository.getActiveTask()
        val pendingIntent = getPendingIntent(activeTask)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_notifications_active)
                setContentIntent(pendingIntent)
                setContentTitle(activeTask.title)
                setContentText(
                    applicationContext.resources.getString(
                        R.string.notify_content, DateConverter.convertMillsToString(activeTask.date)
                    )
                )
            }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelName
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = notificationBuilder.build()
        notificationManager.notify(activeTask.id, notification)
        return Result.success()
    }
}