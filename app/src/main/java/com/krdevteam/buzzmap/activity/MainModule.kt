package com.krdevteam.buzzmap.activity

import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainModule.MainAbstractModule::class])
class MainModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface MainAbstractModule {
        @ControllerScoped
        @ContributesAndroidInjector
        fun mainController(): MainController
    }
}