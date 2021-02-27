package com.krdevteam.buzzmap.injection.module

import com.krdevteam.buzzmap.controller.news.NewsViewState
import com.krdevteam.buzzmap.injection.scope.AppScoped
import dagger.Module
import dagger.Provides

@Module
class ViewStatesModule {

    @Provides
    @AppScoped
    fun provideNewsViewState(): NewsViewState = NewsViewState()

}