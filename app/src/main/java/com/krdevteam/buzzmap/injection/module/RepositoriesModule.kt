package com.krdevteam.buzzmap.injection.module

import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    @AppScoped
    fun provideFirebaseRepository(): FirebaseRepository = FirebaseRepository()

}