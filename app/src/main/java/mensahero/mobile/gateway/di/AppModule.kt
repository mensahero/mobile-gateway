package mensahero.mobile.gateway.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager
import mensahero.mobile.gateway.data.remote.DynamicApiServiceProvider
import mensahero.mobile.gateway.data.repository.ServerRepository
import mensahero.mobile.gateway.data.repository.SetupRepository
import mensahero.mobile.gateway.domain.usecase.CheckSetupCompletedUseCase
import mensahero.mobile.gateway.domain.usecase.CompleteSetupUseCase
import mensahero.mobile.gateway.domain.usecase.TestServerConnectionUseCase
import mensahero.mobile.gateway.domain.usecase.TestWebSocketConnectionUseCase
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies across the app
 * Includes all repositories and use cases
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
    @Singleton
    fun provideServerRepository(
        apiServiceProvider: DynamicApiServiceProvider,
        preferencesManager: PreferencesManager
    ): ServerRepository {
        return ServerRepository(apiServiceProvider, preferencesManager)
    }

    // ==================== USE CASES ====================

    @Provides
    fun provideCheckSetupCompletedUseCase(
        repository: SetupRepository
    ): CheckSetupCompletedUseCase {
        return CheckSetupCompletedUseCase(repository)
    }

    @Provides
    fun provideCompleteSetupUseCase(
        setupRepository: SetupRepository
    ): CompleteSetupUseCase {
        return CompleteSetupUseCase(setupRepository)
    }

    @Provides
    fun provideTestServerConnectionUseCase(
        serverRepository: ServerRepository
    ): TestServerConnectionUseCase {
        return TestServerConnectionUseCase(serverRepository)
    }

    @Provides
    fun provideTestWebSocketConnectionUseCase(
        serverRepository: ServerRepository
    ): TestWebSocketConnectionUseCase {
        return TestWebSocketConnectionUseCase(serverRepository)
    }
}