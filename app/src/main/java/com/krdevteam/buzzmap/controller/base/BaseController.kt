package com.krdevteam.buzzmap.controller.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.krdevteam.buzzmap.controller.map.MapViewModel
import com.krdevteam.buzzmap.entity.News
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseController<V: BaseViewModel<S>, S: ViewState>(
    @LayoutRes private val layoutRes: Int,
    args: Bundle? = null
) : RefWatchingController(args) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected open val title: String? = null

    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                onViewCreated(view)
                observeState()
                viewModel.checkUserLoggedIn()
//                toolbar?.let { configureToolbar(it) }
            }
        })
    }

    abstract var viewModel: V

    abstract fun updateUi(state: S)

    private val observer: Observer<ViewState> = Observer<ViewState> { state -> updateUi(state as S) }

    private fun observeState() {
        viewModel.getState().observeForever(observer)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate(layoutRes, container, false)
    }

    open fun onViewCreated(view: View) = Unit

    override fun onAttach(view: View) {
//        toolbar?.let { configureToolbar(it) }
        super.onAttach(view)
    }

    override fun onChangeStarted(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeStarted(changeHandler, changeType)

        if (changeType.isEnter) {
            var parentController = parentController
            while (parentController != null) {
                if (parentController is BaseController<*, *> && parentController.title != null) {
                    return
                }
                parentController = parentController.parentController
            }
            Timber.d("onChangeStarted $parentController")
//            toolbar?.let { configureMenu(it) }
        }
    }

    open fun configureToolbar(toolbar: Toolbar) {
        val title = title ?: return

        var parentController = parentController
        while (parentController != null) {
            if (parentController is BaseController<*, *> && parentController.title != null) {
                return
            }
            parentController = parentController.parentController
        }

        toolbar.title = title
    }

    open fun configureMenu(toolbar: Toolbar) {
        toolbar.menu.clear()
    }

    protected fun KClass<*>.start(clearingLast: Boolean, extras: Bundle) {
        Timber.d("Start Intent")
        val intent = Intent(activity, this.java)
        intent.putExtras(extras)
        startActivity(intent)

        viewModel.resetNewActivity()
        if (clearingLast) activity?.finish()
    }
}