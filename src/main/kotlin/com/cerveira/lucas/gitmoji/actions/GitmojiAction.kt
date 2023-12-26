package com.cerveira.lucas.gitmoji.actions

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.data.gitmojis
import com.cerveira.lucas.gitmoji.notifications.sendNotification
import com.intellij.ide.TextCopyProvider
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.speedSearch.SpeedSearchUtil.applySpeedSearchHighlighting
import com.intellij.util.containers.nullize
import com.intellij.util.ui.JBUI
import com.intellij.vcs.commit.message.CommitMessageInspectionProfile
import java.awt.Point
import javax.swing.JList
import javax.swing.ListSelectionModel

class GitmojiAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val commitMessage = event.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) as? CommitMessage

        when {
            commitMessage != null -> {
                createPopup(project, commitMessage, gitmojis).show(
                    RelativePoint(
                        commitMessage.editorField, Point(0, JBUI.scale(16))
                    )
                )
            }
        }
    }

    private fun createPopup(
        project: Project, commitMessage: CommitMessage, gitmojis: List<Gitmoji>
    ): JBPopup {
        val font = commitMessage.editorField.editor?.colorsScheme?.getFont(EditorFontType.PLAIN)
        val selectedMessage: Gitmoji? = null

        return JBPopupFactory.getInstance().createPopupChooserBuilder(gitmojis).setVisibleRowCount(5).setFont(font)
            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
            .setItemChosenCallback { sendNotification(it, project) }
            .setRenderer(GitmojiColoredListCellRenderer())
            .setNamerForFiltering { "${it.code} ${it.description}" }.setAutoPackHeightOnFiltering(false).createPopup()
            .apply {
                setDataProvider { dataId ->
                    when (dataId) {
                        // default list action does not work as "CopyAction" is invoked first, but with other copy provider
                        PlatformDataKeys.COPY_PROVIDER.name -> object : TextCopyProvider() {
                            override fun getTextLinesToCopy() = listOfNotNull(selectedMessage?.code).nullize()
                        }

                        else -> null
                    }
                }
            }
    }


}

class GitmojiColoredListCellRenderer : ColoredListCellRenderer<Gitmoji>() {
    override fun customizeCellRenderer(
        list: JList<out Gitmoji>, value: Gitmoji, index: Int, selected: Boolean, hasFocus: Boolean
    ) {
        append(value.value, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        appendTextPadding(8)
        append(value.code, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
        appendTextPadding(8)
        append(value.description, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        applySpeedSearchHighlighting(list, this, true, selected)
    }
}
