package com.yuoyama12.mywordbook.di

import android.content.Context
import com.yuoyama12.mywordbook.data.WordbookDao
import com.yuoyama12.mywordbook.data.WordbookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideWordbookDateBase(
        @ApplicationContext context: Context
    ): WordbookDatabase {
        return WordbookDatabase.getInstance(context)
    }

    @Provides
    fun provideWordbookDao(wordbookDataBase: WordbookDatabase): WordbookDao {
        return wordbookDataBase.wordbookDao()
    }
}