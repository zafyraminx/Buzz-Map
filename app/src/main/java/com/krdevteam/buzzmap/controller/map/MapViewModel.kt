package com.krdevteam.buzzmap.controller.map

import android.annotation.SuppressLint
import com.bluelinelabs.conductor.Controller
import com.google.android.gms.maps.OnMapReadyCallback
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

@AppScoped
class MapViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    viewState: MapViewState
): BaseViewModel<MapViewState>(firebaseRepository, viewState) {

//    fun getMapCallBack()
//    {
//        viewState.onMapReadyCallback = repository.onMapReadyCallback
//    }

    fun getNewsList() {
        Timber.d("getNewsList")
        firebaseRepository.user()?.uid?.let { firebaseRepository.observeNews(it) }
        viewState.newsLiveData = firebaseRepository.newsLiveData
        Timber.d("getNewsList ${viewState.newsLiveData}")
        updateUi()
    }

    @SuppressLint("MissingPermission")
    fun setGoogleMap():OnMapReadyCallback
    {
        return OnMapReadyCallback { googleMap ->
            viewState.map = googleMap
            viewState.map?.uiSettings?.isMyLocationButtonEnabled = false
            viewState.map?.isMyLocationEnabled = true
        }
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
}