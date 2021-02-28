package com.krdevteam.buzzmap.controller.register

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [RegisterModule.RegisterAbstractModule::class])
class RegisterModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface RegisterAbstractModule {
        @ControllerScoped
        @ContributesAndroidInjector
        fun registerController(): RegisterController
    }
}
