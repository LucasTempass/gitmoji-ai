package com.cerveira.lucas.gitmoji.settings

import com.cerveira.lucas.gitmoji.bundle.Bundle.message
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.ComponentWithEmptyText
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class AppSettingsConfigurable : BoundConfigurable(message("settings.title")) {

    private val apiKeyPasswordField = JBPasswordField()
    private val gitmojiValueComboBox = ComboBox<AppSettings.GitmojiProperty>(
        DefaultComboBoxModel(
            arrayOf(
                AppSettings.GitmojiProperty.CODE,
                AppSettings.GitmojiProperty.EMOJI,
                AppSettings.GitmojiProperty.NAME
            )
        )
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

                cell(gitmojiValueComboBox)
                    .bindItem(
                        { AppSettings.instance.getGitmojiProperty() },
                        { AppSettings.instance.setGitmojiProperty(it) })
                    .align(Align.FILL)
            }

            row {
                comment(message("settings.general.gitmoji-value.helper-text"))
            }

        }
    }

}

fun <T> Cell<T>.placeholder(placeholder: String): Cell<T> where T : JComponent, T : ComponentWithEmptyText {
    this.component.emptyText.text = placeholder
    return this
}