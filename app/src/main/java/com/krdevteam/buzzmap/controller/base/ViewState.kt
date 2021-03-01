package com.krdevteam.buzzmap.controller.base

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import kotlin.reflect.KClass

open class ViewState (
        var newActivity: KClass<*>? = null,
        var clearActivityOnIntent: Boolean = false,
        var controller: Controller? = null,
        var showMenu: Boolean = false,
        var userType: String = "Editor",
        open var listItem: ArrayList<Any> = ArrayList(),
        var intentExtras: Bundle = Bundle()
) {
}