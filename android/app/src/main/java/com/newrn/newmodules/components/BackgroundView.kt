package com.newrn.newmodules.components

import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper

class BackgroundView(private val context: ThemedReactContext): AppCompatTextView(context) {
    fun setBackgroundColor(color: String?) {
        setBackgroundColor(Color.parseColor(color))
        val dispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, id)
        dispatcher.let {
            it?.dispatchEvent(OnChangeBackgroundEvent(UIManagerHelper.getSurfaceId(context), id, color))
        }
    }
}