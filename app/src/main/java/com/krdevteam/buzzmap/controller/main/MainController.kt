package com.krdevteam.buzzmap.controller.main

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.bluelinelabs.conductor.*
import com.bluelinelabs.conductor.viewpager.RouterPagerAdapter
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.article.ArticleController
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.map.MapController
import com.krdevteam.buzzmap.controller.news.NewsController
import com.krdevteam.buzzmap.controller.profile.ProfileController
import com.krdevteam.buzzmap.entity.TabItem
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_LOGIN_CONTROLLER
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_NEWS_CONTROLLER
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_PROFILE_CONTROLLER
import kotlinx.android.synthetic.main.controller_main.view.*
import kotlinx.android.synthetic.main.fragment_lobby.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ActivityScoped
class MainController : BaseController<MainControllerViewModel, MainControllerViewState>(R.layout.controller_main),
    ProfileController.OnUpdateControllerListener, NewsController.OnUpdateControllerListener {

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: MainControllerViewModel


    private val pagerAdapter = object : RouterPagerAdapter(this) {
        override fun configureRouter(router: Router, position: Int) {
            if (!router.hasRootController()) {
                val page = this@MainController.getController(position)?.controller
                page?.let { RouterTransaction.with(it) }?.let { router.setRoot(it) }
            }
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = this@MainController.getController(position)?.title
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
    }

    private fun getController(position: Int): TabItem?
    {
        return when (position) {
//            2 -> TabItem("Article",ArticleController())
            1 -> NewsController(this).getInstance()?.let { TabItem("News", it) }
            0 -> MapController().getInstance()?.let { TabItem("Map", it) }
            else -> ProfileController(this).getInstance()?.let { TabItem("Profile", it) }
        }
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
        view.controller_main_view_pager.adapter = this.pagerAdapter
        view.controller_main_tab_layout.setupWithViewPager(view.controller_main_view_pager)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: MainControllerViewState) {
        Timber.d("UpdateUI")
        if (state.controller != null)
        {
            Timber.d("controller :${state.controller}")
                viewController(state.controller!!)
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        Timber.d("onDestroyView")
    }

    override fun onUpdateUI(tag: String) {
        if (tag == TAG_PROFILE_CONTROLLER)
            viewModel.checkUserLoggedIn()
        else if (tag == TAG_NEWS_CONTROLLER)
            ArticleController().getInstance()?.let { viewController(it,tag) }
    }

    private fun viewController(controller: Controller) {
        Timber.d("controller: $controller")
        viewModel.clearState()
        router.pushController(RouterTransaction.with(controller).tag(TAG_LOGIN_CONTROLLER))
    }

    private fun viewController(controller: Controller, tag:String) {
        Timber.d("controller: $controller $tag")
        router.pushController(RouterTransaction.with(controller))
    }
}