package com.krdevteam.buzzmap.controller.main

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import com.krdevteam.buzzmap.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AppScoped
class MainControllerViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    viewState: MainControllerViewState
): BaseViewModel<MainControllerViewState>(firebaseRepository, viewState) {

    override fun checkUserLoggedIn() {
        Timber.d("checkUserLoggedIn")
        if (firebaseRepository.user() == null) {
            viewState.controller = LoginController()
            updateUi()
        }
    }

    private fun validate(roomId: String): Boolean {

        if (roomId.isEmpty()) {
            viewState.errorMessage = "Please enter a Room ID"
            return false
        }

        viewState.errorMessage = ""
        return true
    }

    private fun launchLoginController() {
        viewState.controller = LoginController()
        updateUi()
    }

    fun clearState() {
        viewState.controller = null
        viewState.newActivity = null
        viewState.clearActivityOnIntent = false
        viewState.controller = null
        viewState.showMenu = false
        viewState.userType = "Editor"
    }
}