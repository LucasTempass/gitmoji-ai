package com.cerveira.lucas.gitmoji.service

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.cerveira.lucas.gitmoji.data.Gitmoji
import com.cerveira.lucas.gitmoji.data.gitmojis
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlin.math.pow
import kotlin.math.sqrt

@Service(Service.Level.APP)
public class AIService {

    companion object {
        val instance: AIService
            get() = ApplicationManager.getApplication().getService(AIService::class.java)

    }

    suspend fun generateSuggestedEmoji(message: String): String {
        val openAI = OpenAI(getConfiguration())

        val request = EmbeddingRequest(
            ModelId("text-embedding-ada-002"),
            input = listOf(message),
        )

        val response = openAI.embeddings(request)

        val messageEmbedding = response.embeddings.first().embedding

        val suggestedGitmoji: Gitmoji = gitmojis.maxBy {
            calculateCosineSimilarity(
                messageEmbedding,
                it.embedding
            )
        }

        return suggestedGitmoji.value;
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


    private fun getConfiguration(): OpenAIConfig {
        return OpenAIConfig(
            token = "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",
            host = OpenAIHost.OpenAI,
        )
    }

}