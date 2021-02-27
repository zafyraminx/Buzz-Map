package com.krdevteam.buzzmap.controller.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import javax.inject.Inject

abstract class BaseController<V: BaseViewModel<S>, S: ViewState>(
    @LayoutRes private val layoutRes: Int,
    args: Bundle? = null
) : RefWatchingController(args) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected open val title: String? = null

    protected abstract fun updateUi(state: S)

    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                onViewCreated(view)

//                toolbar?.let { configureToolbar(it) }
            }
        })
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
}