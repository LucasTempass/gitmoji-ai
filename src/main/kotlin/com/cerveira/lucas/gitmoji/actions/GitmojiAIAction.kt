package com.cerveira.lucas.gitmoji.actions

import com.aallam.openai.api.exception.OpenAIAPIException
import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.notifications.sendErrorNotification
import com.cerveira.lucas.gitmoji.service.AIService
import com.cerveira.lucas.gitmoji.ui.EmojiSelectorPopup.Companion.displayEmojiSelectorPopup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class GitmojiAIAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val data = event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) ?: return

        displaySuggestedEmojis(project, data as CommitMessage)
    }

    private fun displaySuggestedEmojis(
        project: Project,
        commitMessage: CommitMessage
    ) {
        runModalTask(
            "Generating Emoji Suggestions", project
        ) {
            val openAIService = AIService.instance

            val suggestedEmojis: List<Gitmoji>? = runBlocking(Dispatchers.IO) {
                try {
                    val generatedEmojis = openAIService.generateSuggestedEmoji(
                        commitMessage.text,
                        "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"
                    )

                    return@runBlocking generatedEmojis;
                } catch (e: Exception) {
                    if (e is OpenAIAPIException) {
                        handleOpenAIAPIException(e, project)
                        return@runBlocking null;
                    }

                    // exceptions related to misconfiguration of the plugin
                    if (e is IllegalStateException) {
                        sendErrorNotification(
                            "OpenAI API key not found",
                            "Please add your OpenAI API key in the settings",
                            project
                        )

                        return@runBlocking null;
                    }

                    // generic exception handling
                    sendErrorNotification(
                        "There was an error generating the suggested emojis",
                        "",
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
            401 -> sendErrorNotification("Invalid OpenAI API key", "", project)
            404 -> sendErrorNotification("OpenAI API not found", "", project)
            429 -> sendErrorNotification("OpenAI API rate limit exceeded", "", project)
            500 -> sendErrorNotification("OpenAI API internal server error", "", project)
            503 -> sendErrorNotification("OpenAI API service unavailable", "", project)
            else -> sendErrorNotification(
                "There was an error generating the suggested emojis", "",
                project,
            )
        }
    }

}

