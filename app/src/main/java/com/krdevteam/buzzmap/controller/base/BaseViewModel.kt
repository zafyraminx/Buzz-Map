package com.krdevteam.buzzmap.controller.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

abstract class BaseViewModel<S: ViewState>(
    val firebaseRepository: FirebaseRepository,
    var viewState: S): ViewModel() {

    private val stateLiveData = MutableLiveData<ViewState>()

    private val networkJob = Job()
    protected val networkScope = CoroutineScope(Dispatchers.IO + networkJob)

    open fun checkUserLoggedIn() {
        Timber.d("checkUserLoggedIn")
        if (firebaseRepository.user() == null) {
            viewState.controller = LoginController()
            updateUi()
        }
    }

    fun handleSignOut() {
        firebaseRepository.auth.signOut()
        checkUserLoggedIn()
    }

    fun getState(): MutableLiveData<ViewState> {
        return this.stateLiveData
    }

    fun resetNewActivity() {
        viewState.newActivity = null
        updateUi()
    }

    fun updateUi() {
        Timber.d("updateUi")
        stateLiveData.postValue(viewState)
    }

}