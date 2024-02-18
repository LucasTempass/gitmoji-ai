package com.cerveira.lucas.gitmoji.ui

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.data.gitmojis
import com.cerveira.lucas.gitmoji.settings.AppSettings
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.ObjectUtils.sentinel
import com.intellij.util.ui.JBUI.scale
import java.awt.Point
import javax.swing.ListSelectionModel

class EmojiSelectorPopup {

    companion object {
        fun displayEmojiSelectorPopup(
            commitMessage: CommitMessage, gitmojisOptions: List<Gitmoji>, project: Project
        ) {
            createPopup(commitMessage, gitmojisOptions, project).showInBestPositionFor(
                commitMessage.editorField.editor!!
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
                .addListener(
                    object : JBPopupListener {
                        override fun beforeShown(event: LightweightWindowEvent) {
                            val popup = event.asPopup()

                            val relativePoint = RelativePoint(commitMessage.editorField, Point(0, -scale(3)))

                            val screenPoint =
                                Point(relativePoint.screenPoint).apply { translate(0, -popup.size.height) }

                            popup.setLocation(screenPoint)
                        }
                    }
                )

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

            val textToInsert = "${extractProperty(gitmoji)}$suffix"

            val currentAppliedGitmoji = gitmojis.firstOrNull { currentMessage.contains(extractProperty(it)) }

            val message = when (currentAppliedGitmoji) {
                null -> textToInsert + currentMessage
                else -> currentMessage.replaceFirst(
                    extractProperty(currentAppliedGitmoji),
                    extractProperty(gitmoji)
                )
            }

            commitMessage.setCommitMessage(message)
        }

        private fun extractProperty(gitmoji: Gitmoji): String {
            val property = AppSettings.instance.getGitmojiProperty();

            return when (property) {
                AppSettings.GitmojiProperty.EMOJI -> gitmoji.value
                AppSettings.GitmojiProperty.NAME -> gitmoji.name
                AppSettings.GitmojiProperty.CODE -> gitmoji.code
            }
        }
    }

}