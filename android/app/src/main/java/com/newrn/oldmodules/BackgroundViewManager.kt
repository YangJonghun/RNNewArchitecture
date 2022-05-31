package com.newrn.oldmodules

import android.graphics.Color
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter


class BackgroundViewManager(private val context: ReactApplicationContext): SimpleViewManager<BackgroundView>() {
    companion object {
        private const val SET_CHANGE_BACKGROUND_COLOR = 1
        private const val ON_CHANGE_BACKGROUND_COLOR = "onChangeBackground"
    }

    override fun getName() = "OldBackgroundView"
    override fun createViewInstance(reactContext: ThemedReactContext) = BackgroundView(reactContext)
    override fun getCommandsMap() = mutableMapOf("setBackgroundColor" to SET_CHANGE_BACKGROUND_COLOR)

    @ReactProp(name = "color")
    fun setColor(view: BackgroundView, value: String?) = view.setTextColor(Color.parseColor(value))

    @ReactProp(name = "step", defaultInt = 0)
    fun setStep(view: BackgroundView, value: Int) = view.setText("Step: $value")

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, MutableMap<String, String>>? =
            mutableMapOf(ON_CHANGE_BACKGROUND_COLOR to
                    mutableMapOf("registrationName" to ON_CHANGE_BACKGROUND_COLOR)
            )

    private fun onChangeBackgroundColor(view:BackgroundView, color: String) {
        val eventData = Arguments.createMap()
        eventData.putString("color", color)
        context.getJSModule(RCTEventEmitter::class.java).receiveEvent(view.id, ON_CHANGE_BACKGROUND_COLOR, eventData)
    }

    override fun receiveCommand(root: BackgroundView, commandId: String, args: ReadableArray?) {
        super.receiveCommand(root, commandId, args)
        val commandIdInt = Integer.parseInt(commandId)
        if (commandIdInt == SET_CHANGE_BACKGROUND_COLOR) {
            val color = args?.getString(0) ?: "black";
            root.setBackgroundColor(Color.parseColor(color))
            onChangeBackgroundColor(root, color)
        }
    }
}