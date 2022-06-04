package es.javier.cappcake.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.javier.cappcake.data.repositories.ImplActivityRepository
import es.javier.cappcake.domain.repositories.ActivityRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ActivityRepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindActivityRepository(repository: ImplActivityRepository) : ActivityRepository

}