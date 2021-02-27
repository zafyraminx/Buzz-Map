package com.krdevteam.buzzmap.controller.news

import com.krdevteam.buzzmap.injection.providers.BaseResourceProvider
import com.krdevteam.buzzmap.injection.providers.ResourceProvider
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.injection.scope.FragmentScoped
import com.krdevteam.buzzmap.activity.MainActivity
import com.krdevteam.buzzmap.controller.map.MapController
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [NewsModule.NewsAbstractModule::class])
class NewsModule {

    @ActivityScoped
    @Provides
    internal fun provideResourceProvider(context: MainActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Module
    interface NewsAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        fun mapController(): MapController
    }
}