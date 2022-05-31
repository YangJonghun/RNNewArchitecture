package com.newrn.newmodules.components

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class OnChangeBackgroundEvent(surfaceId:Int, viewTag: Int, private val mColor: String?): Event<OnChangeBackgroundEvent>(surfaceId, viewTag) {
    companion object {
        const val EVENT_NAME = "topChangeBackground" // must start with prefix "top"
    }

    override fun getEventName() = EVENT_NAME

    override fun getEventData(): WritableMap? {
        val eventData = Arguments.createMap()
        eventData.putString("color", mColor)
        return eventData
    }
}