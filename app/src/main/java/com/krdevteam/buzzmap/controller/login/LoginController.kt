package com.krdevteam.buzzmap.controller.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.article.ArticleController
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_MAIN_CONTROLLER
import kotlinx.android.synthetic.main.controller_login.view.*
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class LoginController : BaseController<LoginViewModel, LoginViewState>(R.layout.controller_login)
{
    private var singleInstance: LoginController? = null

    fun getInstance(): LoginController? {
        if (singleInstance == null) singleInstance = LoginController()
        return singleInstance
    }

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: LoginViewModel

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
        view.controller_login_button_email_login.setOnClickListener {
            viewModel.handleSubmitButtonClicked(
                    view.controller_login_edittext_email.text.toString(),
                    view.controller_login_edittext_password.text.toString())
        }
        view.controller_login_textview_register.setOnClickListener { viewModel.handleRegisterTextviewClicked() }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: LoginViewState) {
        Timber.d("updateUi")
        view?.controller_login_button_email_login?.isEnabled = state.submitEnabled
        view?.controller_login_textview_error_login?.text = state.errorMessage
        if (state.controller!=null)
        {
            Timber.d("controller: ${state.controller}")
            if (state.controller == MainController())
            {
                viewModel.clearState()
                router.popCurrentController()
            }
            else
            {
                viewController(state.controller!!, state.controllerTag)
            }
        }
    }

    private fun viewController(controller: Controller, tag:String) {
        Timber.d("controller: $controller")
        viewModel.clearState()
        router.pushController(RouterTransaction.with(controller))
    }
}