package com.krdevteam.buzzmap.mvvm.activities.login

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.ControllerScoped
import com.krdevteam.buzzmap.injection.scope.FragmentScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [LoginModule.LoginAbstractModule::class])
class LoginModule {

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
