package com.krdevteam.buzzmap.injection.module

import com.krdevteam.buzzmap.controller.map.MapController
import com.krdevteam.buzzmap.controller.map.MapControllerComponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MapControllerComponent::class])
abstract class ControllerBindingModule {

    @Binds
    @IntoMap
    @ClassKey(MapController::class)
    internal abstract fun bindLobbyControllerInjectorFactory(factory: MapControllerComponent.Factory): AndroidInjector.Factory<*>
}