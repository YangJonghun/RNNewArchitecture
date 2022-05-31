package com.newrn.oldmodules

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class AnswerModule(private val context: ReactApplicationContext): ReactContextBaseJavaModule(context) {
    override fun getName() = "OldAnswerModule"

    // isBlockingSynchronousMethod을 줄 수 있지만 권장되지 않음 - 어차피 JS에서 await으로 받아야함
    @ReactMethod(isBlockingSynchronousMethod = true)
    fun sendQuestionSync(question: String) = 42.0

    @ReactMethod
    fun sendQuestion(question: String, promise: Promise) = promise.resolve(42.0)
}