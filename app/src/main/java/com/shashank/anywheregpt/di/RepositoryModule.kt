package com.shashank.anywheregpt.di

import com.shashank.anywheregpt.data.repositories.ChatRepository
import com.shashank.anywheregpt.data.repositories.IChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindChatRepository(
        chatRepository: ChatRepository,
    ): IChatRepository

}