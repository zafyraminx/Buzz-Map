package com.krdevteam.buzzmap.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.HasControllerInjector
import com.krdevteam.buzzmap.util.AppConstants.Companion.TAG_MAIN_CONTROLLER
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
            router!!.setRoot(RouterTransaction.with(MainController()).tag(TAG_MAIN_CONTROLLER))
        }

        setupPermissions()
    }

    private fun setupPermissions() {
        val permissionCoarseLoc = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissionCoarseLoc != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                Toast.makeText(this,"Location Settings is required",Toast.LENGTH_LONG).show()
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun controllerInjector(): DispatchingAndroidInjector<Controller> {
        return dispatchingControllerInjector
    }
}