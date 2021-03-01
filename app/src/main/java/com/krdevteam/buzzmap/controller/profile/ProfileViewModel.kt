package com.krdevteam.buzzmap.controller.profile

import androidx.lifecycle.MutableLiveData
import com.bluelinelabs.conductor.Controller
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import javax.inject.Inject

@AppScoped
class ProfileViewModel @Inject constructor(
        firebaseRepository: FirebaseRepository,
        viewState: ProfileViewState
): BaseViewModel<ProfileViewState>(firebaseRepository, viewState) {

    override fun checkUserLoggedIn() {
        if (firebaseRepository.user() == null) {
            viewState.controller = LoginController()
            updateUi()
        }
    }

    fun loadDisplayedList() {
        val itemList: ArrayList<Any> = ArrayList()
        itemList.add("Email: ${firebaseRepository.user()?.email}")
        if (firebaseRepository.user()?.displayName != null || firebaseRepository.user()?.displayName != "null")
            itemList.add("Name: ${firebaseRepository.user()?.displayName}")
        itemList.add("Logout")
        viewState.listItem = itemList
        updateUi()
    }

    fun handleProfileItemClicked(item: String) {
        when (item) {
            "Logout" -> signOut()
        }
    }

    private fun signOut() {
        clearState()
        firebaseRepository.auth.signOut()
        viewState.controller = LoginController()
        val liveData = MutableLiveData<Controller>()
        liveData.apply { LoginController() }
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