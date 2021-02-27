package com.krdevteam.buzzmap.controller.map

import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ControllerScoped
@Subcomponent
interface MapControllerComponent : AndroidInjector<MapController> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MapController>
}