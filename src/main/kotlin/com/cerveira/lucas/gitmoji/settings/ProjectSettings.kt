package com.cerveira.lucas.gitmoji.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = ProjectSettings.SERVICE_NAME, storages = [Storage("GitmojiAI.xml")])
@Service(Service.Level.PROJECT)
class ProjectSettings : PersistentStateComponent<ProjectSettings?> {

    companion object {
        const val SERVICE_NAME =
            "com.cerveira.lucas.gitmoji.settings.com.cerveira.lucas.gitmoji.settings.ProjectSettings"
    }

    override fun getState() = this

    override fun loadState(state: ProjectSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
