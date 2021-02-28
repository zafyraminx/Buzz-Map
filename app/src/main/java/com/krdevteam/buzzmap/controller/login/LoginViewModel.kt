package com.krdevteam.buzzmap.controller.login

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
class LoginViewModel @Inject constructor(
        firebaseRepository: FirebaseRepository,
        viewState: LoginViewState
): BaseViewModel<LoginViewState>(firebaseRepository, viewState) {

    override fun checkUserLoggedIn() {

        if (firebaseRepository.user() != null) {
            viewState.clearActivityOnIntent = true
            updateUi()
        }
    }

    fun handleSubmitButtonClicked(username: String, password: String) {
        loginWithEmailAndPassword(username, password)
    }

    fun handleRegisterTextviewClicked() {
        viewState.controller = RegisterController()
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

    private fun loginWithEmailAndPassword(email: String, password: String) {

        if (!validate(email, password)) {
            updateUi()
            return
        }

        viewState.submitEnabled = false
        updateUi()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            viewState.errorMessage = "Please enter the correct email and password"
            viewState.submitEnabled = true
            updateUi()
        }

        networkScope.launch(errorHandler) {
            Timber.d("LOGIN: ${firebaseRepository.login(email, password)}")
            viewState.controller = MainController()
            viewState.clearActivityOnIntent = true
            updateUi()
        }
    }

    private fun validate(email: String, password: String): Boolean {

        if (email.isEmpty() || password.isEmpty()) {
            this.viewState.errorMessage = "Please fill out all the fields"
            return false
        }

        if (!isEmailValid(email)) {
            this.viewState.errorMessage = "Please enter a valid email"
            return false
        }

        if (password.length < 6) {
            this.viewState.errorMessage = "Password must be at least 6 characters long"
            return false
        }

        viewState.errorMessage = ""
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }


}