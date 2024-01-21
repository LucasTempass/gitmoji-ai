package com.cerveira.lucas.gitmoji.service

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.data.Gitmojis
import com.cerveira.lucas.gitmoji.data.gitmojis
import com.cerveira.lucas.gitmoji.settings.AppSettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlin.math.pow
import kotlin.math.sqrt

@Service(Service.Level.APP)
class AIService {

    val data: Gitmojis = Gitmojis()

    companion object {
        val instance: AIService
            get() = ApplicationManager.getApplication().getService(AIService::class.java)

    }

    suspend fun generateSuggestedEmoji(message: String, token: String?): List<Gitmoji> {
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("An OpenAI API token is required to use this plugin.")
        }

        val messageEmbedding = getEmbedding(token, message)

        val suggestedGitmojis: List<Gitmoji> = data.gitmojis.sortedBy {
            calculateCosineSimilarity(it.embedding, messageEmbedding)
        }.reversed()

        return suggestedGitmojis.take(5)
    }

    private suspend fun getEmbedding(token: String, message: String): List<Double> {
        val cache = AppSettings.instance.getCache()

        val currentAppliedGitmoji = gitmojis.firstOrNull { message.contains(it.value) }

        // removes applied emoji to compare with cached embeddings and avoid bias
        val cleanedMessage = currentAppliedGitmoji?.let {
            message.replace(it.value, "")
        } ?: message

        if (cache.containsKey(cleanedMessage)) {
            return cache[cleanedMessage]!!
        }

        val openAI = OpenAI(getConfiguration(token))

        val request = EmbeddingRequest(
            ModelId("text-embedding-ada-002"),
            input = listOf(cleanedMessage),
        )

        val response = openAI.embeddings(request)

        val messageEmbedding = response.embeddings[0].embedding

        // stores embedding in cache for future use
        cache[cleanedMessage] = messageEmbedding

        return messageEmbedding
    }

    private fun calculateCosineSimilarity(
        embbeding1: List<Double>,
        embbeding2: List<Double>
    ): Double {
        var dotProduct = 0.0
        var norm1 = 0.0
        var norm2 = 0.0

        for (i in embbeding1.indices) {
            dotProduct += embbeding1[i] * embbeding2[i]
            norm1 += embbeding1[i].pow(2.0)
            norm2 += embbeding2[i].pow(2.0)
        }

        return dotProduct / (sqrt(norm1) * sqrt(norm2))
    }


    private fun getConfiguration(token: String): OpenAIConfig {
        return OpenAIConfig(
            token = token,
            host = OpenAIHost.OpenAI,
        )
    }

}