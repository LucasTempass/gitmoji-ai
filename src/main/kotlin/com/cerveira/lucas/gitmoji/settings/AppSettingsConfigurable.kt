package com.cerveira.lucas.gitmoji.settings

import com.cerveira.lucas.gitmoji.bundle.Bundle.message
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class AppSettingsConfigurable : BoundConfigurable(message("settings.title")) {

    private val apiKeyPasswordField = JBPasswordField()

    override fun createPanel() = panel {
        row {
            label(message("settings.general.api-key")).widthGroup("label")

            with(cell(apiKeyPasswordField).bindText(
                { AppSettings.instance.getToken().orEmpty() },
                { AppSettings.instance.setToken(it) }
            ).align(Align.FILL)) {
                component.emptyText.setText(message("settings.general.api-key.placeholder"))
            }
        }

        row {
            comment(message("settings.general.api-key.helper-text"))
        }

        row {
            comment(message("settings.general.api-key.disclaimer")).bold()
        }
    }

}
