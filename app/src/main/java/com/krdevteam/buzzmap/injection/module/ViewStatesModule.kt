package com.krdevteam.buzzmap.injection.module

import com.krdevteam.buzzmap.controller.article.ArticleViewState
import com.krdevteam.buzzmap.controller.login.LoginViewState
import com.krdevteam.buzzmap.controller.main.MainControllerViewState
import com.krdevteam.buzzmap.controller.map.MapViewState
import com.krdevteam.buzzmap.controller.news.NewsViewState
import com.krdevteam.buzzmap.controller.register.RegisterViewState
import com.krdevteam.buzzmap.injection.scope.AppScoped
import dagger.Module
import dagger.Provides

@Module
class ViewStatesModule {

    @Provides
    @AppScoped
    fun provideMainControllerViewState(): MainControllerViewState = MainControllerViewState()

    @Provides
    @AppScoped
    fun provideNewsViewState(): NewsViewState = NewsViewState()

    @Provides
    @AppScoped
    fun provideMapViewState(): MapViewState = MapViewState()

    @Provides
    @AppScoped
    fun provideArticleViewState(): ArticleViewState = ArticleViewState()

    @Provides
    @AppScoped
    fun provideLoginViewState(): LoginViewState = LoginViewState()

    @Provides
    @AppScoped
    fun provideRegisterViewState(): RegisterViewState = RegisterViewState()

}