package com.cerveira.lucas.gitmoji.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = AppSettings.SERVICE_NAME, storages = [Storage("GitmojiAI.xml")]
)
class AppSettings : PersistentStateComponent<AppSettings> {

    private var openAIToken: String? = null
    private var cache: MutableMap<String, List<Double>> = mutableMapOf()
    private var isInsertEmoji: Boolean = true

    companion object {
        const val SERVICE_NAME = "com.cerveira.lucas.gitmoji.settings.com.cerveira.lucas.gitmoji.settings.AppSettings"

        val instance: AppSettings
            get() = ApplicationManager.getApplication().getService(AppSettings::class.java)

    }

    override fun getState() = this

    override fun loadState(state: AppSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun setToken(token: String?) {
        openAIToken = token
    }

    fun getToken(): String? {
        return openAIToken
    }

    fun getCache(): MutableMap<String, List<Double>> {
        return cache
    }

    fun isInsertEmoji(): Boolean {
        return isInsertEmoji
    }

    fun setInsertEmoji(isEmoji: Boolean) {
        // TODO update on Settings panel
        this.isInsertEmoji = isEmoji
    }

}
