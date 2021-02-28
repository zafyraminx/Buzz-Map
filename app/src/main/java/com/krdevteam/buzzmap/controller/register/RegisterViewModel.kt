package com.krdevteam.buzzmap.controller.register

import android.view.MenuItem
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@AppScoped
class RegisterViewModel @Inject constructor(
        firebaseRepository: FirebaseRepository,
        viewState: RegisterViewState
): BaseViewModel<RegisterViewState>(firebaseRepository, viewState) {

    override fun checkUserLoggedIn() {

//        if (firebaseRepository.user() != null) {
//            viewState.controller = LoginController()
//            updateUi()
//        }
    }

    fun handleSubmitButtonClicked(username: String, password: String, confirmPassword: String, userType: String) {
        registerWithEmailAndPassword(username, password, confirmPassword, userType)
    }

    fun handleLoginTextviewClicked() {
        viewState.controller = LoginController()
        updateUi()
    }

    fun handleUserTypeClicked() {
        viewState.showMenu = true
        updateUi()
    }

    fun handleMenuItemClicked(item: MenuItem?) {
        when (item!!.itemId) {
            R.id.option_editor -> {
                viewState.userType = "Editor"
                viewState.showMenu = false
            }
            R.id.option_journalist -> {
                viewState.userType = "Journalist"
                viewState.showMenu = false
            }
        }
        updateUi()
    }

    private fun registerWithEmailAndPassword(username: String, password: String, confirmPassword: String, userType: String) {

        if (!validate(username, password, confirmPassword)) {
            updateUi()
            return
        }

        viewState.submitEnabled = false
        updateUi()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            viewState.errorMessage = "Account associated with this email already exists"
            viewState.submitEnabled = true
            updateUi()
        }

        networkScope.launch(errorHandler) {
            firebaseRepository.register(username, password)
            viewState.clearActivityOnIntent = true
            addUser(userType)
        }
    }

    private fun addUser(userType: String)
    {
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            viewState.errorMessage = "Account associated with this email already exists"
            viewState.submitEnabled = true
            updateUi()
        }

        networkScope.launch(errorHandler) {
            firebaseRepository.addUser(userType)
            viewState.controller = MainController()
            updateUi()
        }
    }

    private fun validate(email: String, password: String, confirmPassword: String): Boolean {

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
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

        if (password != confirmPassword) {
            this.viewState.errorMessage = "Password confirmation doesn't match"
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

    fun clearState() {
        viewState.controller = null
        viewState.newActivity = null
        viewState.clearActivityOnIntent = false
        viewState.controller = null
        viewState.showMenu = false
        viewState.userType = "Editor"
    }


}