package com.newrn.newmodules.components

import android.graphics.Color
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.NewBackgroundViewManagerDelegate
import com.facebook.react.viewmanagers.NewBackgroundViewManagerInterface

@ReactModule(name = BackgroundViewManager.REACT_CLASS)
class BackgroundViewManager() : SimpleViewManager<BackgroundView>(), NewBackgroundViewManagerInterface<BackgroundView> {
    companion object {
        const val REACT_CLASS = "NewBackgroundView"
    }

    private val mDelegate by lazy {
        NewBackgroundViewManagerDelegate(this)
    }

    override fun getDelegate() = mDelegate
    override fun createViewInstance(reactContext: ThemedReactContext) = BackgroundView(reactContext)
    override fun getName() = REACT_CLASS

    @ReactProp(name = "color")
    override fun setColor(view: BackgroundView, value: String?) = view.setTextColor(Color.parseColor(value))

    @ReactProp(name = "step", defaultInt = 0)
    override fun setStep(view: BackgroundView, value: Int) = view.setText("Step: $value")

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, MutableMap<String, String>>? =
            mutableMapOf(OnChangeBackgroundEvent.EVENT_NAME to
                    mutableMapOf("registrationName" to "onChangeBackground")
            )

    override fun setBackgroundColor(view: BackgroundView, value: String?) = view.setBackgroundColor(value)

    override fun receiveCommand(root: BackgroundView, commandId: String, args: ReadableArray?) {
        mDelegate.receiveCommand(root, commandId, args)
    }
}