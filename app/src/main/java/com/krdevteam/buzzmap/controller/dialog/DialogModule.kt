package com.krdevteam.buzzmap.controller.dialog

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [DialogModule.LoginAbstractModule::class])
class DialogModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface LoginAbstractModule {
        @ControllerScoped
        @ContributesAndroidInjector
        fun loginController(): LoginController
    }
}
