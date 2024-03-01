package com.cerveira.lucas.gitmoji.settings

import com.cerveira.lucas.gitmoji.bundle.Bundle.message
import com.cerveira.lucas.gitmoji.settings.AppSettings.GitmojiProperty.*
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.ComponentWithEmptyText
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JList

class AppSettingsConfigurable : BoundConfigurable(message("settings.title")) {

    private val apiKeyPasswordField = JBPasswordField()

    private val properties = arrayOf(
        CODE,
        EMOJI,
        NAME
    )

    override fun createPanel() = panel {
        group("OpenAI") {
            row {
                label(message("settings.general.api-key")).widthGroup("label").bold()

                cell(apiKeyPasswordField).bindText({ AppSettings.instance.getToken().orEmpty() },
                    { AppSettings.instance.setToken(it) })
                    .placeholder(message("settings.general.api-key.placeholder")).align(Align.FILL)
            }

            row {
                comment(message("settings.general.api-key.helper-text"))
            }

            row {
                comment(message("settings.general.api-key.disclaimer")).bold()
            }
        }
        group("Usage") {
            row {
                label(message("settings.general.gitmoji-value")).widthGroup("label").bold()

                comboBox(
                    DefaultComboBoxModel(properties),
                    // TODO extract
                    renderer = object : ColoredListCellRenderer<AppSettings.GitmojiProperty>() {
                        override fun customizeCellRenderer(
                            list: JList<out AppSettings.GitmojiProperty>,
                            value: AppSettings.GitmojiProperty,
                            index: Int,
                            selected: Boolean,
                            hasFocus: Boolean
                        ) {
                            append(formatGitmojitProperty(value), SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
                        }
                    }
                ).bindItem(
                    { AppSettings.instance.getGitmojiProperty() },
                    { AppSettings.instance.setGitmojiProperty(it) }
                ).align(Align.FILL)
            }

            row {
                comment(message("settings.general.gitmoji-value.helper-text"))
            }

        }
    }

}

fun formatGitmojitProperty(prop: AppSettings.GitmojiProperty): String {
    // TODO extract to enum and bundle
    return when (prop) {
        CODE -> "Code"
        EMOJI -> "Emoji"
        NAME -> "Name"
    }
}

fun <T> Cell<T>.placeholder(placeholder: String): Cell<T> where T : JComponent, T : ComponentWithEmptyText {
    this.component.emptyText.text = placeholder
    return this
}