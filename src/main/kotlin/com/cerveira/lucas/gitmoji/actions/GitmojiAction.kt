package com.cerveira.lucas.gitmoji.actions

import com.cerveira.lucas.gitmoji.data.gitmojis
import com.cerveira.lucas.gitmoji.ui.EmojiSelectorPopup.Companion.displayEmojiSelectorPopup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage


class GitmojiAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val commitMessage = event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) ?: return

        displayEmojiSelectorPopup(commitMessage as CommitMessage, gitmojis)
    }

}

