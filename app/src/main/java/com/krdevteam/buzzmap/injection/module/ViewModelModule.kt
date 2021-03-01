package com.krdevteam.buzzmap.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krdevteam.buzzmap.controller.article.ArticleViewModel
import com.krdevteam.buzzmap.controller.login.LoginController
import com.krdevteam.buzzmap.controller.login.LoginViewModel
import com.krdevteam.buzzmap.controller.main.MainControllerViewModel
import com.krdevteam.buzzmap.controller.map.MapViewModel
import com.krdevteam.buzzmap.controller.news.NewsViewModel
import com.krdevteam.buzzmap.controller.profile.ProfileViewModel
import com.krdevteam.buzzmap.controller.register.RegisterViewModel
import com.krdevteam.buzzmap.injection.ViewModelKey
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @AppScoped
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainControllerViewModel::class)
    abstract fun bindMainControllerViewModel(viewModel: MainControllerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticleViewModel(viewModel: ArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

}