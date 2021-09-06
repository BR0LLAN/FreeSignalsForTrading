package com.devcraft.freesignalsfortrading.app.di

import com.devcraft.freesignalsfortrading.data.ApiConstants
import com.devcraft.freesignalsfortrading.data.mappers.GetQuotesMapper
import com.devcraft.freesignalsfortrading.data.retrofitApi.QuotesApi
import com.devcraft.freesignalsfortrading.presentation.ui.quotes.QuotesViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModule = module {
    viewModel { QuotesViewModel(get(), get()) }
}

val netModule = module {
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient()
    }

    fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )
    }

    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder().baseUrl(ApiConstants.API_BASE_URL).client(okHttpClient).addConverterFactory(converterFactory).build()
    }
    single { provideOkHttpClient() }
    single { provideGsonConverterFactory() }
    single { provideRetrofit(get(), get()) }
}



val netApiModule = module {
    fun provideQuotesApi(retrofit: Retrofit): QuotesApi {
        return retrofit.create(QuotesApi::class.java)
    }

    single { provideQuotesApi(get()) }
}

val repositoryMappersModule = module {
    fun provideGetQuotesMapper(): GetQuotesMapper {
        return GetQuotesMapper()
    }

    single { provideGetQuotesMapper() }
}

