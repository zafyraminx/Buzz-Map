package com.krdevteam.buzzmap.controller.map

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MapModule.MapAbstractModule::class])
class MapModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface MapAbstractModule {
        @ControllerScoped
        @ContributesAndroidInjector
        fun mapController(): MapController
    }
}