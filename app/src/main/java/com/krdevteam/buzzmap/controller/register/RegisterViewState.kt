package com.krdevteam.buzzmap.controller.register

import com.bluelinelabs.conductor.Controller
import com.krdevteam.buzzmap.controller.base.ViewState
import kotlin.reflect.KClass

class RegisterViewState(
    var submitEnabled: Boolean = true,
    newActivity: KClass<*>? = null,
    controller: Controller? = null,
    clearActivityOnIntent: Boolean = false,
    var errorMessage: String = ""
): ViewState(
    newActivity, clearActivityOnIntent
)
