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
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_LOGIN_CONTROLLER
import kotlinx.android.synthetic.main.controller_main.view.*
import kotlinx.android.synthetic.main.fragment_lobby.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@ActivityScoped
class MainController : BaseController<MainControllerViewModel, MainControllerViewState>(R.layout.controller_main) {

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: MainControllerViewModel

    private val pagerAdapter = object : RouterPagerAdapter(this) {
        override fun configureRouter(router: Router, position: Int) {
            if (!router.hasRootController()) {
                val page = this@MainController.getController(position)
                router.setRoot(RouterTransaction.with(page))
            }
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = this@MainController.getTitle(position)
    }

    private fun getController(position: Int):Controller
    {
        return when (position) {
            2 -> {
                ArticleController()
            }
            1 -> {
                NewsController()
            }
            0 -> {
                MapController()
            }
            else -> {
                ArticleController()
            }
        }
    }

    private fun getTitle(position: Int):String
    {
        return when (position) {
            2 -> "Article"
            1 -> "News"
            0 -> "Map"
            else -> "Profile"
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

    private fun viewController(controller: Controller) {
        Timber.d("controller: $controller")
        viewModel.clearState()
        router.pushController(RouterTransaction.with(controller).tag(TAG_LOGIN_CONTROLLER))

    }
}