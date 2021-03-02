package com.krdevteam.buzzmap.controller.news

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.controller.article.ArticleController
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.controller.base.ViewState
import com.krdevteam.buzzmap.controller.main.MainControllerViewState
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import com.krdevteam.buzzmap.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AppScoped
class NewsViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    viewState: NewsViewState
): BaseViewModel<NewsViewState>(firebaseRepository, viewState) {

    fun handleEnterButton(roomId: String) {
        Timber.d("handleEnterButton $roomId")
        if (!validate(roomId)) {
            updateUi()
            return
        }

        viewState.enterBtnEnabled = false
        updateUi()

        val errorHandler = CoroutineExceptionHandler { _, exception ->

            exception.printStackTrace()
            viewState.errorMessage = "Something went wrong. Please try again"
            viewState.enterBtnEnabled = true
            updateUi()
        }

        networkScope.launch(errorHandler) {

            firebaseRepository.addUserToRoom(roomId)
            launchRoomActivity(roomId)
        }

    }

    private fun launchRoomActivity(roomId: String) {
        viewState.enterBtnEnabled = true
        viewState.clearRoomTextfield = true
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

//    fun getNewsList() {
//        Timber.d("getNewsList")
//
//        viewState.enterBtnEnabled = false
//        updateUi()
//
//        val errorHandler = CoroutineExceptionHandler { _, exception ->
//
//            exception.printStackTrace()
//            viewState.errorMessage = "Something went wrong. Please try again"
//            viewState.enterBtnEnabled = true
//            updateUi()
//        }
//
//        networkScope.launch(errorHandler) {
//
//            firebaseRepository.observeNews()
//        }
//    }

    fun getNewsList() {
        Timber.d("getNewsList")
        firebaseRepository.user()?.uid?.let { firebaseRepository.observeNews(it) }
        viewState.newsLiveData = firebaseRepository.newsLiveData
        Timber.d("getNewsList ${viewState.newsLiveData}")
        updateUi()
    }

    fun clearState() {
        viewState.controller = null
        viewState.newActivity = null
        viewState.clearActivityOnIntent = false
        viewState.controller = null
        viewState.showMenu = false
        viewState.listItem = ArrayList()
        viewState.userType = "Editor"
    }

    fun handleItemClick(news: Any) {
        viewState.controller = ArticleController().getInstance()
        updateUi()
    }
}