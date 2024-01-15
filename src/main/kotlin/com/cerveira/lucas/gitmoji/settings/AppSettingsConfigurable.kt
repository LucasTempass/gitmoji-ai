package com.cerveira.lucas.gitmoji.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class AppSettingsConfigurable : BoundConfigurable("Gitmoji") {

    private val apiKeyPasswordField = JBPasswordField()

    override fun createPanel() = panel {
        row {
            label("OpenAI API Key").widthGroup("label")

            with(cell(apiKeyPasswordField).bindText(
                { AppSettings.instance.getToken().orEmpty() },
                { AppSettings.instance.setToken(it) }
            ).align(Align.FILL)) {
                component.emptyText.setText("sk-xxxxxxxxxxxxxxxxxxxxxx")
            }
        }

        row {
            comment("You can get your OpenAI API key from <a href=\"https://beta.openai.com/account/api-keys\">the OpenAI API dashboard</a>.")
        }

        row {
            comment("This information is stored locally and is not shared with anyone.").bold()
        }
    }

}
