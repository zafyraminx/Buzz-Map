package com.krdevteam.buzzmap.controller.login

import com.krdevteam.buzzmap.controller.base.ViewState


class LoginViewState (
    var submitEnabled: Boolean = true,
    var errorMessage: String = "",
    var controllerTag: String = ""
): ViewState()

