package com.krdevteam.buzzmap.injection.component

import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.controller.article.ArticleController
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.injection.module.*
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.controller.map.MapController
import com.krdevteam.buzzmap.controller.news.NewsController
import com.krdevteam.buzzmap.controller.register.RegisterController
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScoped
@Component(modules = [
    AppModule::class,
    ActivityBindingModule::class,
    ControllerBindingModule::class,
    AndroidSupportInjectionModule::class,
    ViewModelModule::class,
    RepositoriesModule::class,
    ViewStatesModule::class
])
interface AppComponent: AndroidInjector<Application> {
    fun inject(controller: MainController)
    fun inject(controller: MapController)
    fun inject(controller: NewsController)
    fun inject(controller: ArticleController)
    fun inject(controller: LoginController)
    fun inject(controller: RegisterController)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}