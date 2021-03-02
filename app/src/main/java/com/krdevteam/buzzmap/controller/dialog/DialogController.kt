package com.krdevteam.buzzmap.controller.dialog

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.register.RegisterViewModel
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class DialogController(args: Bundle) : BaseController<DialogViewModel, DialogViewState>(R.layout.controller_dialog, args) {

    constructor(title: CharSequence, description: CharSequence) : this(
        bundleOf(
            KEY_TITLE to title,
            KEY_DESCRIPTION to description
        )
    )

    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: DialogViewModel

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
//        binding.title.text = args.getCharSequence(KEY_TITLE)
//        binding.description.text = args.getCharSequence(KEY_DESCRIPTION)
//        binding.description.movementMethod = LinkMovementMethod.getInstance()
//
//        binding.dismiss.setOnClickListener { router.popController(this) }
//        binding.dialogBackground.setOnClickListener { router.popController(this) }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: DialogViewState) {
    }

    companion object {
        private const val KEY_TITLE = "DialogController.title"
        private const val KEY_DESCRIPTION = "DialogController.description"
    }
}