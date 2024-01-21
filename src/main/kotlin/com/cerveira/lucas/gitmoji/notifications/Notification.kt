package com.cerveira.lucas.gitmoji.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun sendErrorNotification(title: String, content: String, project: Project? = null) {
    sendNotification(title, content, project, NotificationType.ERROR)
}

private fun sendNotification(
    title: String, content: String, project: Project?, notificationType: NotificationType
) {
    val notificationManager =
        NotificationGroupManager.getInstance().getNotificationGroup("gitmoji.ai.notification.general")

    val intellijNotification = notificationManager.createNotification(
        title, content, notificationType
    )

    intellijNotification.notify(project)
}