package com.cerveira.lucas.gitmoji.actions

import com.cerveira.lucas.gitmoji.data.gitmojis
import com.cerveira.lucas.gitmoji.ui.EmojiSelectorPopup.Companion.displayEmojiSelectorPopup
import com.intellij.ide.HelpTooltip
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage


class GitmojiAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(event: AnActionEvent) {
        val commitMessage = event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) ?: return

        displayEmojiSelectorPopup(commitMessage as CommitMessage, gitmojis)
    }

    override fun update(e: AnActionEvent) {
        val project = e.project

        e.presentation.isVisible = project != null

        val tooltip = HelpTooltip()

        e.presentation.putClientProperty(ActionButton.CUSTOM_HELP_TOOLTIP, tooltip.apply {
            setTitle("Gitmoji")
            setDescription("Select an emoji from the Gitmoji specification to add to your commit message")
        })
    }


}

