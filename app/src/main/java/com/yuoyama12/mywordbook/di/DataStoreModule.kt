package com.yuoyama12.mywordbook.di

import android.content.Context
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Singleton
    @Provides
    fun providesDataStoreManager(
        @ApplicationContext context: Context
    ): DataStoreManager =
        DataStoreManager(context)

}