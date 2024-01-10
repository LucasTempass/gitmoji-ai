package com.cerveira.lucas.gitmoji.ui

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.ui.JBUI
import java.awt.Point
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import javax.swing.ListSelectionModel

class EmojiSelectorPopup {

    companion object {
        fun displayEmojiSelectorPopup(
            commitMessage: CommitMessage,
            gitmojisOptions: List<Gitmoji>
        ) {
            createPopup(commitMessage, gitmojisOptions).show(
                RelativePoint(
                    commitMessage.editorField, Point(0, JBUI.scale(8))
                )
            )
        }

        private fun createPopup(
            commitMessage: CommitMessage, gitmojis: List<Gitmoji>
        ): JBPopup {
            val font = commitMessage.editorField.editor?.colorsScheme?.getFont(EditorFontType.PLAIN)

            val builder = JBPopupFactory.getInstance().createPopupChooserBuilder(gitmojis).setFont(font)
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
                .setVisibleRowCount(7)
                .setItemChosenCallback(::onChoose)
                .setRenderer(GitmojiColoredListCellRenderer())
                .setNamerForFiltering { "${it.code} ${it.description}" }
                .setAutoPackHeightOnFiltering(false)

            return builder.createPopup()
        }

        private fun onChoose(value: Gitmoji) {
            // TODO implement paste to commit message
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(StringSelection(value.code), null)
        }

    }

}