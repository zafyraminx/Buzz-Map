package com.krdevteam.buzzmap.controller.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.ed2e.ed2eapp.adapter.ViewHolderFactory
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.controller.news.NewsController
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_PROFILE_CONTROLLER
import com.krdevteam.buzzmap.util.adapter.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.controller_profile.view.*
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class ProfileController() : BaseController<ProfileViewModel, ProfileViewState>(R.layout.controller_profile) {
    private var singleInstance: ProfileController? = null

    fun getInstance(): ProfileController? {
        if (singleInstance == null) singleInstance = ProfileController()
        return singleInstance
    }

    private var mLifecycleRegistry = LifecycleRegistry(this)

    constructor(targetController: OnUpdateControllerListener) : this() {
        check(targetController is Controller)
        setTargetController(targetController)
    }

    interface OnUpdateControllerListener {
        fun onUpdateUI(tag:String)
    }

    @Inject
    override lateinit var viewModel: ProfileViewModel


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
        viewModel.clearState()
        viewModel.loadDisplayedList()
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry() {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: ProfileViewState) {
        Timber.d("updateUi")
        showItemList(state.listItem)
        if (state.controller != null)
        {
            Timber.d("controller :${state.controller}")
            viewController(state.controller!!)
        }
    }

    private fun showItemList(arrayList: ArrayList<Any>) {
        val adapter = object : BaseRecyclerViewAdapter<Any>(arrayList, object :
                ItemClickListener<Any> {
            override fun onItemClick(v: View, item: Any, position: Int) {
                viewModel.handleProfileItemClicked(item as String)
            }
        }) {
            override fun getLayoutId(position: Int, obj: Any): Int {
                return R.layout.list_item_profile
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return ViewHolderFactory.create(view, viewType)
            }
        }
        view?.controller_profile_recyclerView?.layoutManager = LinearLayoutManager(activity)
        view?.controller_profile_recyclerView?.adapter = adapter
    }

    private fun viewController(controller: Controller) {
        Timber.d("controller: $controller")
        targetController?.let { listener ->
            (listener as OnUpdateControllerListener).onUpdateUI(TAG_PROFILE_CONTROLLER)
            router.popController(this)
        }
        viewModel.clearState()
    }
}