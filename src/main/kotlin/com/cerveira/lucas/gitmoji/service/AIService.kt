package com.cerveira.lucas.gitmoji.service

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

@Service(Service.Level.APP)
public class AIService {

    companion object {
        val instance: AIService
            get() = ApplicationManager.getApplication().getService(AIService::class.java)

    }

    suspend fun generateSuggestedEmoji(prompt: String): String {
        val openAI = OpenAI(getConfiguration())

        val baseSystemPrompt = ChatMessage(
            role = ChatRole.System,
            content = "Your role is to suggest an emoji according to the Gitmoji description given a commit message."
        )

        val chatRequest = ChatMessage(
            role = ChatRole.User,
            content = prompt
        )

        val chatCompletionRequest = ChatCompletionRequest(
            ModelId("gpt-3.5-turbo"),
            listOf(baseSystemPrompt, chatRequest),
            temperature = 0.5,
            topP = 1.0,
            frequencyPenalty = 0.1,
            presencePenalty = 0.1,
            maxTokens = 200,
            n = 1,
        )

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

        val choices = completion.choices

        for (index in choices.indices) {
            println("Choice $index: ${choices[index].message.content}")
        }

        return choices[0].message.content ?: "API returned an empty response."
    }

    private fun getConfiguration(): OpenAIConfig {
        return OpenAIConfig(
            token = "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",
            host = OpenAIHost.OpenAI,
        )
    }

}