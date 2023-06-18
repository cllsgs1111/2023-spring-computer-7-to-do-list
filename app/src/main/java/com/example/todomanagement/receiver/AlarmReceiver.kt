package com.example.todomanagement.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.todomanagement.util.sendNotification

class AlarmReceiver : BroadcastReceiver() {
    //TODO 从intent中分离出标题和内容
    lateinit var title: String
    lateinit var description: String

    override fun onReceive(context: Context, intent: Intent) {
        title = intent.getStringExtra("TITLE").toString()
        description = intent.getStringExtra("DESCRIPTION").toString()

        val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
        ) as NotificationManager

        if (title.isEmpty()) {
            title = "默认标题"
        }
        if (description.isEmpty()) {
            description = "默认内容"
        }

        notificationManager.sendNotification(
                title,
                description,
                context
        )

        //TODO 调用util类中的方法直接发送通知
        //notificationManager.sendNotification(title.value!!, description.value!!, app)
        //notificationManager.cancelNotifications()
    }
}