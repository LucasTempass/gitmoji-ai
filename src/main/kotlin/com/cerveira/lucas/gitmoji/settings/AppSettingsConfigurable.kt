package com.cerveira.lucas.gitmoji.settings

import com.cerveira.lucas.gitmoji.bundle.Bundle.message
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.ComponentWithEmptyText
import javax.swing.JComponent

class AppSettingsConfigurable : BoundConfigurable(message("settings.title")) {

    private val apiKeyPasswordField = JBPasswordField()

    override fun createPanel() = panel {
        group("OpenAI") {
            row {
                label(message("settings.general.api-key")).widthGroup("label")

                cell(apiKeyPasswordField)
                    .bindText(
                        { AppSettings.instance.getToken().orEmpty() },
                        { AppSettings.instance.setToken(it) }
                    )
                    .placeholder(message("settings.general.api-key.placeholder"))
                    .align(Align.FILL)

            }

            row {
                comment(message("settings.general.api-key.helper-text"))
            }

            row {
                comment(message("settings.general.api-key.disclaimer")).bold()
            }
        }
        group("Usage") {



        }
    }

}

fun <T> Cell<T>.placeholder(placeholder: String): Cell<T> where T : JComponent, T : ComponentWithEmptyText {
    this.component.emptyText.text = placeholder
    return this
}