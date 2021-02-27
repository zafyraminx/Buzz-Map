package com.krdevteam.buzzmap.controller.main

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager.RouterPagerAdapter
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.news.NewsViewModel
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.controller.map.MapController
import kotlinx.android.synthetic.main.controller_main.view.*
import kotlinx.android.synthetic.main.fragment_lobby.view.*
import java.util.*
import javax.inject.Inject


@ActivityScoped
class MainController : BaseController<MainCViewModel, MainCViewState>(R.layout.controller_main) {

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    lateinit var viewModel: NewsViewModel

    private val pagerAdapter = object : RouterPagerAdapter(this) {
        override fun configureRouter(router: Router, position: Int) {
            if (!router.hasRootController()) {
                val page = this@MainController.getController(position)
                router.setRoot(RouterTransaction.with(page))
            }
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = "Page $position"
    }

    private fun getController(position: Int):Controller
    {
        return when (position) {
            2 -> {
                MapController()
            }
            1 -> {
                MapController()
            }
            else -> {
                MapController()
            }
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

    private fun viewModelOwnerController() {
        router.pushController(RouterTransaction.with(MapController()))
    }

    override fun updateUi(state: MainCViewState) {
        TODO("Not yet implemented")
    }
}