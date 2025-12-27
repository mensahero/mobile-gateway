package mensahero.mobile.gateway.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager
import mensahero.mobile.gateway.data.repository.SetupRepository
import mensahero.mobile.gateway.domain.usecase.CheckSetupCompletedUseCase
import mensahero.mobile.gateway.domain.usecase.CompleteSetupUseCase
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies across the app
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideSetupRepository(
        preferencesManager: PreferencesManager
    ): SetupRepository {
        return SetupRepository(preferencesManager)
    }

    @Provides
    fun provideCheckSetupCompletedUseCase(
        repository: SetupRepository
    ): CheckSetupCompletedUseCase {
        return CheckSetupCompletedUseCase(repository)
    }

    @Provides
    fun provideCompleteSetupUseCase(
        repository: SetupRepository
    ): CompleteSetupUseCase {
        return CompleteSetupUseCase(repository)
    }
}