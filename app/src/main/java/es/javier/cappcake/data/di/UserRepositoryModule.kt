package es.javier.cappcake.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.javier.cappcake.data.repositories.ImplUserRepository
import es.javier.cappcake.domain.repositories.UserRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserRepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindUserRepository(userRepository: ImplUserRepository) : UserRepository

}