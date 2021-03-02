package com.krdevteam.buzzmap.controller.dialog

import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.controller.base.ViewState
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.controller.main.MainControllerViewState
import com.krdevteam.buzzmap.controller.register.RegisterController
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@AppScoped
class DialogViewModel @Inject constructor(
        firebaseRepository: FirebaseRepository,
        viewState: DialogViewState
): BaseViewModel<DialogViewState>(firebaseRepository, viewState) {


}