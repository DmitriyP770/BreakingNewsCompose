package com.example.breakingnewscompose.di

import com.example.breakingnewscompose.data.repository.ArticleRepositoryImpl
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        articleRepositoryImpl : ArticleRepositoryImpl
    ) : ArticleRepository
}