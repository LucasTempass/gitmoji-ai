package com.cerveira.lucas.gitmoji.actions

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.notifications.sendNotification
import com.cerveira.lucas.gitmoji.service.AIService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.speedSearch.SpeedSearchUtil.applySpeedSearchHighlighting
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.awt.Point
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import javax.swing.JList
import javax.swing.ListSelectionModel


class GitmojiAction : AnAction() {

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
                createEmojiSelector(commitMessage, project, suggestedEmojis)
            }
        }
    }


    private fun createEmojiSelector(
        commitMessage: CommitMessage,
        project: Project,
        gitmojisOptions: List<Gitmoji>
    ) {
        createPopup(commitMessage, gitmojisOptions, project).show(
            RelativePoint(
                commitMessage.editorField, Point(0, JBUI.scale(16))
            )
        )
    }

    private fun createPopup(
        commitMessage: CommitMessage, gitmojis: List<Gitmoji>, project: Project
    ): JBPopup {
        val font = commitMessage.editorField.editor?.colorsScheme?.getFont(EditorFontType.PLAIN)

        val builder = JBPopupFactory.getInstance().createPopupChooserBuilder(gitmojis).setFont(font)
            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
            .setVisibleRowCount(5)
            .setItemChosenCallback {
                copyToClipboard(it.value)
            }
            .setRenderer(GitmojiColoredListCellRenderer())
            .setNamerForFiltering { "${it.code} ${it.description}" }
            .setAutoPackHeightOnFiltering(false)

        return builder.createPopup()
    }

    private fun copyToClipboard(str: String?) {
        // TODO implement paste to commit message
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(str), null)
    }

}

class GitmojiColoredListCellRenderer : ColoredListCellRenderer<Gitmoji>() {
    override fun customizeCellRenderer(
        list: JList<out Gitmoji>, value: Gitmoji, index: Int, selected: Boolean, hasFocus: Boolean
    ) {
        append(value.value, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        append(" ", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES)
        append(value.code, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
        append(" - ", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES)
        append(value.description, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        applySpeedSearchHighlighting(list, this, true, selected)
    }
}
