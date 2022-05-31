package com.newrn.newmodules.modules

import com.facebook.react.bridge.ReactApplicationContext
import com.newrn.codegen.NativeAnswerModuleSpec;

class NativeAnswerModule(reactContext: ReactApplicationContext): NativeAnswerModuleSpec(reactContext) {
    companion object {
        const val NAME = "NewAnswerModule"
    }
    override fun getName() = NAME
    override fun sendQuestion(input: String) = 42.0
}