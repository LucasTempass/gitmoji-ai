package com.cerveira.lucas.gitmoji.settings

import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.function.Function

@State(
    name = AppSettings.SERVICE_NAME, storages = [Storage("GitmojiAI.xml")]
)
class AppSettings : PersistentStateComponent<AppSettings> {

    enum class GitmojiProperty(val extractor: Function<Gitmoji, String>) {
        EMOJI(Gitmoji::value),
        CODE(Gitmoji::code),
        NAME(Gitmoji::name);
    }

    private var openAIToken: String? = null
    private var gitmojiProperty: GitmojiProperty = GitmojiProperty.EMOJI
    private var cache: MutableMap<String, List<Double>> = mutableMapOf()

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

    fun getGitmojiProperty(): GitmojiProperty {
        return gitmojiProperty
    }

    fun setGitmojiProperty(gitmojiProperty: GitmojiProperty?) {
        // EMOJI is the default value
        this.gitmojiProperty = gitmojiProperty ?: GitmojiProperty.EMOJI
    }

}
