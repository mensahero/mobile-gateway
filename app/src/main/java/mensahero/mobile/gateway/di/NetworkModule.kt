package mensahero.mobile.gateway.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import mensahero.mobile.gateway.BuildConfig
import dagger.hilt.components.SingletonComponent
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager
import mensahero.mobile.gateway.data.remote.DynamicApiServiceProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network module for Retrofit, OkHttpClient, and API services
 * Uses dynamic base URLs from user preferences instead of hardcoded values
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provide Gson instance with custom configuration
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    /**
     * Provide HTTP logging interceptor for debugging
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Set level based on build type
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * Provide OkHttpClient with timeouts and logging
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Provide Dynamic API Service Provider
     * This replaces the static Retrofit instance
     * Base URL is determined at runtime from saved preferences
     */
    @Provides
    @Singleton
    fun provideDynamicApiServiceProvider(
        preferencesManager: PreferencesManager,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): DynamicApiServiceProvider {
        return DynamicApiServiceProvider(
            preferencesManager = preferencesManager,
            okHttpClient = okHttpClient,
            gson = gson
        )
    }
}