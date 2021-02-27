package com.krdevteam.buzzmap.controller.main

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import com.krdevteam.buzzmap.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AppScoped
class MainCViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    viewState: MainCViewState
): BaseViewModel<MainCViewState>(firebaseRepository, viewState) {

    fun handleEnterButton(roomId: String) {
        Timber.d("handleEnterButton $roomId")
        if (!validate(roomId)) {
            updateUi()
            return
        }

        updateUi()

        val errorHandler = CoroutineExceptionHandler { _, exception ->

            exception.printStackTrace()
            viewState.errorMessage = "Something went wrong. Please try again"
            updateUi()
        }

        networkScope.launch(errorHandler) {

            firebaseRepository.addUserToRoom(roomId)
            launchRoomActivity(roomId)
        }

    }

    private fun launchRoomActivity(roomId: String) {
        viewState.newActivity = MainActivity::class
        viewState.clearActivityOnIntent = false
        viewState.intentExtras.putString(AppConstants.INTENT_EXTRA_ROOMID, roomId)
        updateUi()
    }

    private fun validate(roomId: String): Boolean {

        if (roomId.isEmpty()) {
            viewState.errorMessage = "Please enter a Room ID"
            return false
        }

        viewState.errorMessage = ""
        return true
    }

    fun checkRoomIdPreference(roomId: String?) {
        roomId?.let { launchRoomActivity(it) }
    }
}