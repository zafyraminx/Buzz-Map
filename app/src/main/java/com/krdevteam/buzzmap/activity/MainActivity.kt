package com.krdevteam.buzzmap.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.HasControllerInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), HasControllerInjector {

    @Inject lateinit var dispatchingControllerInjector: DispatchingAndroidInjector<Controller>

    private var router: Router? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = findViewById<View>(R.id.controller_container) as ViewGroup
        router = attachRouter(this, container, savedInstanceState)
        if (!router!!.hasRootController()) {
            router!!.setRoot(RouterTransaction.with(MainController()))
        }
    }

    override fun controllerInjector(): DispatchingAndroidInjector<Controller> {
        return dispatchingControllerInjector
    }
}