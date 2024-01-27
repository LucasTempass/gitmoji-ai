package com.cerveira.lucas.gitmoji.actions

import com.aallam.openai.api.exception.OpenAIAPIException
import com.cerveira.lucas.gitmoji.bundle.Bundle.message
import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.notifications.sendErrorNotification
import com.cerveira.lucas.gitmoji.service.AIService
import com.cerveira.lucas.gitmoji.settings.AppSettings
import com.cerveira.lucas.gitmoji.ui.EmojiSelectorPopup.Companion.displayEmojiSelectorPopup
import com.intellij.ide.HelpTooltip
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.IconManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.swing.Icon


class GitmojiAIAction : AnAction() {

    val disabledIcon: Icon = IconManager.getInstance().getIcon(
        "/icons/blue-action-disabled.svg", this::class.java
    )

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project

        val token = AppSettings.instance.getToken()

        val isTokenInvalid = token.isNullOrEmpty()

        val isCommitMessageEmpty: Boolean = getCommitMessage(e)?.text?.isEmpty() ?: true

        e.presentation.isEnabled = !isCommitMessageEmpty && !isTokenInvalid && project != null

        val tooltip = HelpTooltip()

        val description = if (isTokenInvalid) {
            message("actions.gitmoji.ai.description.invalid-token")
        } else if (isCommitMessageEmpty) {
            message("actions.gitmoji.ai.description.empty-commit")
        } else {
            message("actions.gitmoji.ai.description")
        }

        e.presentation.putClientProperty(ActionButton.CUSTOM_HELP_TOOLTIP, tooltip.apply {
            setTitle(message("actions.gitmoji.ai.title"))
            setDescription(description)
        })

        e.presentation.disabledIcon = disabledIcon
    }


    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val data = getCommitMessage(event) ?: return

        displaySuggestedEmojis(project, data)
    }

    private fun displaySuggestedEmojis(
        project: Project, commitMessage: CommitMessage
    ) {
        runModalTask(
            message("actions.gitmoji.ai.loading"), project
        ) {
            val openAIService = AIService.instance

            val suggestedEmojis: List<Gitmoji>? = runBlocking(Dispatchers.IO) {
                try {
                    val generatedEmojis = openAIService.generateSuggestedEmoji(
                        commitMessage.text, AppSettings.instance.getToken()
                    )

                    return@runBlocking generatedEmojis
                } catch (e: Exception) {
                    if (e is OpenAIAPIException) {
                        handleOpenAIAPIException(e, project)
                        return@runBlocking null
                    }

                    // generic exception handling
                    sendErrorNotification(
                        message("actions.gitmoji.ai.error.title"),
                        message("actions.gitmoji.ai.error.description"),
                        project,
                    )

                    return@runBlocking null
                }
            }

            if (suggestedEmojis == null) {
                return@runModalTask
            }

            ApplicationManager.getApplication().invokeLater {
                displayEmojiSelectorPopup(commitMessage, suggestedEmojis, project)
            }
        }
    }

    private fun handleOpenAIAPIException(e: OpenAIAPIException, project: Project) {
        when (e.statusCode) {
            401 -> sendErrorNotification(
                message("actions.gitmoji.ai.error.title"),
                message("actions.gitmoji.ai.error.401.description"),
                project
            )

            429 -> sendErrorNotification(
                message("actions.gitmoji.ai.error.title"),
                message("actions.gitmoji.ai.error.429.description"),
                project
            )

            500 -> sendErrorNotification(
                message("actions.gitmoji.ai.error.title"),
                message("actions.gitmoji.ai.error.500.description"),
                project
            )

            503 -> sendErrorNotification(
                message("actions.gitmoji.ai.error.title"), message("actions.gitmoji.ai.error.503.description"), project
            )

            else -> sendErrorNotification(
                message("actions.gitmoji.ai.error.title"),
                message("actions.gitmoji.ai.error.unknown.description"),
                project,
            )
        }
    }

    private fun getCommitMessage(event: AnActionEvent) =
        event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) as CommitMessage?

}

