package job.hunt.potteredia.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import job.hunt.potteredia.network.HarryPotterApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHarryPotterRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaTypeOrNull()!!)
            )
            .build()

    @Provides
    @Singleton
    fun provideHarryPotterApi(retrofit: Retrofit): HarryPotterApi =
        retrofit.create(HarryPotterApi::class.java)
}
