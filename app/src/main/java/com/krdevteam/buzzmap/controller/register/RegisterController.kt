package com.krdevteam.buzzmap.controller.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.asTransaction
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_LOGIN_CONTROLLER
import kotlinx.android.synthetic.main.controller_register.view.*
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class RegisterController : BaseController<RegisterViewModel, RegisterViewState>(R.layout.controller_register)
{
    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: RegisterViewModel

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

        view.button_register.setOnClickListener { viewModel.handleSubmitButtonClicked(
                view.edittext_email.text.toString(),
                view.edittext_password.text.toString(),
                view.edittext_confirm_password.text.toString(),
                view.button_user_type.text.toString()
        ) }

        view.button_user_type.setOnClickListener { viewModel.handleUserTypeClicked() }
        view.textview_login.setOnClickListener { viewModel.handleLoginTextviewClicked() }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: RegisterViewState) {
        Timber.d("updateUi")
        view?.button_register?.isEnabled = state.submitEnabled
        view?.textview_error_register?.text = state.errorMessage
        view?.button_user_type?.text = state.userType
        if (state.showMenu) view?.button_user_type?.let { showMenu(it) }
        if (state.controller != null)
        {
            Timber.d("controller: ${state.controller}")
            if (state.controller == LoginController())
            {
                viewModel.clearState()
                router.popCurrentController()
            }
            else
            {
                viewController(state.controller!!)
            }
        }
    }

    private fun viewController(controller: Controller) {
        Timber.d("controller: $controller")
        Timber.d("controller1: ${router.backstack.contains(RouterTransaction.Companion.with(controller))}")
        Timber.d("controller2: ${router.backstack.contains(RouterTransaction.with(MainController()))}")
        Timber.d("controller1: ${RouterTransaction.Companion.with(MainController()).controller}")
        Timber.d("controller1: ${MainController()}")
        for (backStackController in router.backstack)
        {

            Timber.d("controller2: ${backStackController.controller}")

            Timber.d("controller2: ${backStackController.controller == controller}")
        }
//        if (!router.backstack.contains(RouterTransaction.with(controller)))
//            router.replaceTopController(RouterTransaction.with(controller))
//        else
        Timber.d("controller2: ${router.getControllerWithTag(TAG_LOGIN_CONTROLLER)}")
        viewModel.clearState()
        router.pushController(RouterTransaction.with(controller))
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(activity!!, v)
        popup.menuInflater.inflate(R.menu.user_type_menu, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            viewModel.handleMenuItemClicked(item)
            true
        }
        popup.show()
    }
}