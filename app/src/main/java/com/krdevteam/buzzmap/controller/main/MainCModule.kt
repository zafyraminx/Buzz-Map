package com.krdevteam.buzzmap.controller.main

import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import com.krdevteam.buzzmap.activity.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainCModule.NewsAbstractModule::class])
class MainCModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface NewsAbstractModule {
        @ControllerScoped
        @ContributesAndroidInjector
        fun MainController(): MainController
    }
}