package com.cerveira.lucas.gitmoji.bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey


private const val BUNDLE = "messages.Bundle"

object Bundle : DynamicBundle(BUNDLE) {
    @JvmStatic

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String) = getMessage(key)

    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String) = getLazyMessage(key)

}