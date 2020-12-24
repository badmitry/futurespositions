package com.badmitry.futurespositions.di.modules

import com.badmitry.futurespositions.mvp.model.api.IIssDataSource
import com.badmitry.futurespositions.mvp.model.api.IMoexDataSource
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Singleton
    @Named("iss")
    @Provides
    fun gson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Singleton
    @Named("moex")
    @Provides
    fun gsonMoex() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Singleton
    @Provides
    fun client() = OkHttpClient.Builder()
        .callTimeout(60, TimeUnit.SECONDS)
        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })
        .build()

    @Singleton
    @Named("iss")
    @Provides
    fun api(@Named("iss") gson: Gson, client: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://iss.moex.com")
        .client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IIssDataSource::class.java)

    @Singleton
    @Named("moex")
    @Provides
    fun moexApi(@Named("moex") gson: Gson, client: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://www.moex.com")
        .client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IMoexDataSource::class.java)
}