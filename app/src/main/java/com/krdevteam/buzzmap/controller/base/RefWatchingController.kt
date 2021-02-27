package com.krdevteam.buzzmap.controller.base

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import leakcanary.AppWatcher
import leakcanary.ObjectWatcher

abstract class RefWatchingController(args: Bundle?) : Controller(args), LifecycleOwner {

    private var hasExited = false
    private val objectWatcher: ObjectWatcher = AppWatcher.objectWatcher

    public override fun onDestroy() {
        super.onDestroy()

        if (hasExited) {
            objectWatcher.expectWeaklyReachable(this, "onDestroy")
        }
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)

        hasExited = !changeType.isEnter
        if (isDestroyed) {
            objectWatcher.expectWeaklyReachable(this, "onDestroy")
        }
    }
}