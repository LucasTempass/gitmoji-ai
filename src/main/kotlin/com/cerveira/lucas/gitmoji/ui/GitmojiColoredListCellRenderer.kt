package com.cerveira.lucas.gitmoji.ui

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.speedSearch.SpeedSearchUtil.applySpeedSearchHighlighting
import javax.swing.JList

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