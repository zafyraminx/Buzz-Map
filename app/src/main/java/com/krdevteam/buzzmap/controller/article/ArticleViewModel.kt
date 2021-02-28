package com.krdevteam.buzzmap.controller.article

import android.annotation.SuppressLint
import com.google.android.gms.maps.OnMapReadyCallback
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import timber.log.Timber
import javax.inject.Inject

@AppScoped
class ArticleViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    viewState: ArticleViewState
): BaseViewModel<ArticleViewState>(firebaseRepository, viewState) {

    fun getNewsList() {
        Timber.d("getNewsList")
        firebaseRepository.observeNews()
        viewState.newsLiveData = firebaseRepository.newsLiveData
        Timber.d("getNewsList ${viewState.newsLiveData}")
        updateUi()
    }

    fun handleSendButton(news: News) {
        sendMessage(news)
        updateUi()
    }

    private fun sendMessage(news: News) {
        firebaseRepository.sendArticle(news)
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