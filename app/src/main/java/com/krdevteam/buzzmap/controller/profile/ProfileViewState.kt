package com.krdevteam.buzzmap.controller.profile

import com.bluelinelabs.conductor.Controller
import com.krdevteam.buzzmap.controller.base.ViewState
import kotlin.reflect.KClass

class ProfileViewState(
        var submitEnabled: Boolean = true,
        newActivity: KClass<*>? = null,
        controller: Controller? = null,
        clearActivityOnIntent: Boolean = false,
        override var listItem: ArrayList<Any> = ArrayList(),
        var errorMessage: String = ""
): ViewState()
