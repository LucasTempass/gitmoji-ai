package com.cerveira.lucas.gitmoji.actions

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.notifications.sendNotification
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
                        commitMessage.text
                    )

                    return@runBlocking generatedEmojis;
                } catch (e: Exception) {
                    sendNotification("There was an error generating the suggested emojis", project)

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

}

