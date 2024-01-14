package com.cerveira.lucas.gitmoji.notifications

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun sendNotification(gitmoji: Gitmoji, project: Project? = null) {

    val notificationManager =
        NotificationGroupManager.getInstance().getNotificationGroup("gitmoji.ai.notification.general")

    val intellijNotification = notificationManager.createNotification(
        "Random Gitmoji", "${gitmoji.value}: ${gitmoji.description}", NotificationType.INFORMATION
    )

    intellijNotification.notify(project)
}

fun sendNotification(
    message: String, project: Project? = null, notificationType: NotificationType = NotificationType.INFORMATION
) {
    sendNotification("Gitmoji AI", message, project, notificationType)
}

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