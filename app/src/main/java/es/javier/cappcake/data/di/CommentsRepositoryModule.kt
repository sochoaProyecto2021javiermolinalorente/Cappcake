package es.javier.cappcake.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.javier.cappcake.data.repositories.ImplCommentsRepository
import es.javier.cappcake.domain.repositories.CommentsRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class CommentsRepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindsCommentsRepository(repository: ImplCommentsRepository) : CommentsRepository

}