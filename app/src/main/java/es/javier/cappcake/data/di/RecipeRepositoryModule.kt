package es.javier.cappcake.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.javier.cappcake.data.repositories.ImplRecipeRepository
import es.javier.cappcake.domain.repositories.RecipeRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RecipeRepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun BindsRecipeRepository(recipeRepository: ImplRecipeRepository) : RecipeRepository

}
