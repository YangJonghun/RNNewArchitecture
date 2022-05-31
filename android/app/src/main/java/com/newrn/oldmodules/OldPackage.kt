package com.newrn.oldmodules

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager

class OldPackage : ReactPackage {
    override fun createViewManagers(reactContext: ReactApplicationContext) = arrayListOf<SimpleViewManager<BackgroundView>>(BackgroundViewManager(reactContext))

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule>
            = arrayListOf<NativeModule>(AnswerModule(reactContext))
}