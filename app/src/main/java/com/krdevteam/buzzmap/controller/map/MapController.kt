package com.krdevteam.buzzmap.controller.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import kotlinx.android.synthetic.main.controller_map.view.*
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class MapController : BaseController<MapViewModel, MapViewState>(R.layout.controller_map) {
    private var singleInstance: MapController? = null

    fun getInstance(): MapController? {
        if (singleInstance == null) singleInstance = MapController()
        return singleInstance
    }

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: MapViewModel

    private val observer: Observer<List<News>> = Observer<List<News>> { t ->
//        setListAdapter(t)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        initLifeCycleRegistry()
        if (!::viewModel.isInitialized) {
            (activity!!.application as Application).appComponent.inject(this)
        }
        return super.onCreateView(inflater, container, savedViewState)
    }

    override fun onViewCreated(view: View) {
        view.controller_map_view.getMapAsync(viewModel.setGoogleMap())
        viewModel.checkUserLoggedIn()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        getNewsList()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        viewModel.viewState.newsLiveData?.removeObserver(observer)
    }

    private fun getNewsList() {
        Timber.d("getNewsList")
        viewModel.getNewsList()
        viewModel.viewState.newsLiveData?.observeForever(observer)
    }

    private fun setMarkers(news:News)
    {
        viewModel.viewState.map?.apply {
            val point = LatLng(news.location.latitude, news.location.longitude)
            addMarker(
                MarkerOptions()
                    .position(point)
                    .title(news.title)
            )
        }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    private fun viewController(controller: Controller) {
        router.pushController(RouterTransaction.with(controller))
    }

    override fun updateUi(state: MapViewState) {
    }
}