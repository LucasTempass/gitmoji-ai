package com.cerveira.lucas.gitmoji.ui

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.data.gitmojis
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.ObjectUtils.sentinel
import com.intellij.util.ui.JBUI
import java.awt.Point
import javax.swing.ListSelectionModel

class EmojiSelectorPopup {

    companion object {
        fun displayEmojiSelectorPopup(
            commitMessage: CommitMessage, gitmojisOptions: List<Gitmoji>, project: Project
        ) {
            createPopup(commitMessage, gitmojisOptions, project).show(
                RelativePoint(
                    commitMessage.editorField, Point(0, JBUI.scale(8))
                )
            )
        }

        private fun createPopup(
            commitMessage: CommitMessage, gitmojis: List<Gitmoji>, project: Project
        ): JBPopup {
            val font = commitMessage.editorField.editor?.colorsScheme?.getFont(EditorFontType.PLAIN)

            val builder = JBPopupFactory.getInstance().createPopupChooserBuilder(gitmojis).setFont(font)
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION).setVisibleRowCount(7)
                .setItemChosenCallback { onChoose(it, commitMessage, project) }
                .setRenderer(GitmojiColoredListCellRenderer())
                .setNamerForFiltering { "${it.code} ${it.description}" }
                .setAutoPackHeightOnFiltering(false)

            return builder.createPopup()
        }

        private fun onChoose(gitmoji: Gitmoji, commitMessage: CommitMessage, project: Project) {
            CommandProcessor.getInstance().executeCommand(project, {
                insertGitmoji(commitMessage, gitmoji)
            }, "", sentinel("Insert Gitmoji into Commit Message"), commitMessage.editorField.document)
        }

        private fun insertGitmoji(
            commitMessage: CommitMessage, gitmoji: Gitmoji
        ) {
            val currentMessage = commitMessage.editorField.text

            val suffix = " "

            val textToInsert = "${gitmoji.value}$suffix"

            val currentAppliedGitmoji = gitmojis.firstOrNull { currentMessage.contains(it.value) }

            val message = when (currentAppliedGitmoji) {
                null -> textToInsert + currentMessage
                else -> currentMessage.replaceFirst(
                    currentAppliedGitmoji.value, gitmoji.value
                )
            }

            commitMessage.setCommitMessage(message)
        }

    }

}