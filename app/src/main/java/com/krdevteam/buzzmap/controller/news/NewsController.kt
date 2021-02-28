package com.krdevteam.buzzmap.controller.news

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.adapter.NewsAdapter
import kotlinx.android.synthetic.main.controller_news.view.*
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class NewsController : BaseController<NewsViewModel, NewsViewState>(R.layout.controller_news)
{
    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: NewsViewModel

    private val observer: Observer<List<News>> = Observer<List<News>> { t -> setListAdapter(t) }

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
        getNewsList()
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
        view?.controller_news_recyclerView?.layoutManager = LinearLayoutManager(activity)
        view?.controller_news_recyclerView?.adapter = adapter
    }

    private fun viewModelOwnerController() {
        router.pushController(RouterTransaction.with(NewsController()))
    }

    override fun updateUi(state: NewsViewState) {
        Timber.d("updateUi")
    }
}