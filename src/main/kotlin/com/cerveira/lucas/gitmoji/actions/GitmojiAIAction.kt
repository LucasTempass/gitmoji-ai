package com.cerveira.lucas.gitmoji.actions

import com.aallam.openai.api.exception.OpenAIAPIException
import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.notifications.sendErrorNotification
import com.cerveira.lucas.gitmoji.service.AIService
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class GitmojiAIAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project

        val token = getToken()

        val isTokenInvalid = token.isNullOrEmpty()

        val isCommitMessageEmpty: Boolean = getCommitMessage(e)?.text?.isEmpty() ?: true

        e.presentation.isEnabled = !isCommitMessageEmpty && !isTokenInvalid
        e.presentation.isVisible = project != null

        val tooltip = HelpTooltip()

        val description = if (isTokenInvalid) {
            "Configure your OpenAI API key in the settings to get emoji suggestions for your commit message"
        } else if (isCommitMessageEmpty) {
            "Write a commit message to get AI generated emoji suggestions"
        } else {
            "Get AI generated emoji suggestions for your commit message"
        }

        e.presentation.putClientProperty(ActionButton.CUSTOM_HELP_TOOLTIP, tooltip.apply {
            setTitle("Generate Gitmoji suggestions")
            setDescription(description)
        })
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
            "Generating Emoji Suggestions", project
        ) {
            val openAIService = AIService.instance

            val suggestedEmojis: List<Gitmoji>? = runBlocking(Dispatchers.IO) {
                try {
                    val generatedEmojis = openAIService.generateSuggestedEmoji(
                        commitMessage.text, getToken()
                    )

                    return@runBlocking generatedEmojis;
                } catch (e: Exception) {
                    if (e is OpenAIAPIException) {
                        handleOpenAIAPIException(e, project)
                        return@runBlocking null;
                    }

                    // generic exception handling
                    sendErrorNotification(
                        "There was an error generating the suggested emojis",
                        "Please try again later or report the issue on GitHub.",
                        project,
                    )

                    return@runBlocking null
                }
            }

            if (suggestedEmojis == null) {
                return@runModalTask
            }

            ApplicationManager.getApplication().invokeLater {
                displayEmojiSelectorPopup(commitMessage, suggestedEmojis)
            }
        }
    }

    private fun handleOpenAIAPIException(e: OpenAIAPIException, project: Project) {
        when (e.statusCode) {
            401 -> sendErrorNotification(
                "There was an error generating the suggested emojis",
                "Update your OpenAI API key in the settings or check if it is properly configured.",
                project
            )

            429 -> sendErrorNotification(
                "There was an error generating the suggested emojis",
                "The OpenAI API rate limit was exceeded, try again later or check your account settings.",
                project
            )

            500 -> sendErrorNotification(
                "There was an error generating the suggested emojis",
                "The OpenAI API returned an internal server error, try again later or check your commit message.",
                project
            )

            503 -> sendErrorNotification(
                "OpenAI API service unavailable",
                "The OpenAI API maybe unavailable, try again later.",
                project
            )

            else -> sendErrorNotification(
                "There was an error generating the suggested emojis",
                "Please try again later or check your internet connection.",
                project,
            )
        }
    }

    private fun getCommitMessage(event: AnActionEvent) =
        event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) as CommitMessage?

    private fun getToken(): String? {
        // TODO get token from settings
        return "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"
    }

}

