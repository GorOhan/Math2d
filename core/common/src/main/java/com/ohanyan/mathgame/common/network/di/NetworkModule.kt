package com.ohanyan.mathgame.common.network.di

import com.ohanyan.mathgame.common.network.ConnectivityManagerNetworkMonitor
import com.ohanyan.mathgame.common.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
