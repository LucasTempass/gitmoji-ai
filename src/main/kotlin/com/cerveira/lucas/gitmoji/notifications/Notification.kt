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