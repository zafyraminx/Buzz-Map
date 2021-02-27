package com.krdevteam.buzzmap.controller.map

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.news.NewsViewModel
import com.krdevteam.buzzmap.controller.news.NewsViewState
import com.krdevteam.buzzmap.util.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_lobby.view.*
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class MapController : BaseController<NewsViewModel, NewsViewState>(R.layout.fragment_lobby) {
    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    lateinit var viewModel: NewsViewModel

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
        view.button_enter?.setOnClickListener {
            Timber.d("click ${viewModel.viewState.messagesLiveData}")
        }
        getNewsList()
    }

    private fun getNewsList() {
        Timber.d("getNewsList")
        viewModel.getNewsList()
        val observer: Observer<List<News>> =
            Observer<List<News>> { t ->
                Timber.d("click ${t?.get(0)?.title}")
                setListAdapter(t)
            }
        viewModel.viewState.messagesLiveData?.observeForever(observer)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.nav_signout -> {
                viewModel.handleSignOut()
                true
            }
            else -> false
        }
    }

    private fun setListAdapter(messages: List<News>) {
        val adapter = NewsAdapter(messages)
        view?.list_chat?.layoutManager = LinearLayoutManager(activity)
        view?.list_chat?.adapter = adapter
    }

    private fun viewModelOwnerController() {
        router.pushController(RouterTransaction.with(MapController()))
    }

    override fun updateUi(state: NewsViewState) {
        TODO("Not yet implemented")
    }
}