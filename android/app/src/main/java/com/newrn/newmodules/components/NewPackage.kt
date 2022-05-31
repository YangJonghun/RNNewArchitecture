package com.newrn.newmodules.components

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager

class NewPackage : ReactPackage {
    override fun createViewManagers(reactContext: ReactApplicationContext) = arrayListOf<SimpleViewManager<BackgroundView>>(BackgroundViewManager())

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> = emptyList()
}