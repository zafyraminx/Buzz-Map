package com.krdevteam.buzzmap.injection.module

import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.activity.MainModule
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun mainActivity(): MainActivity

}