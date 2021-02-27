package com.krdevteam.buzzmap.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krdevteam.buzzmap.controller.news.NewsViewModel
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
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

}