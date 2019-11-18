package com.richardlewan.travelbook.dagger

import com.google.android.gms.maps.model.LatLng
import com.richardlewan.travelbook.service.PersistService
import com.richardlewan.travelbook.service.PersistServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class LocationsModule {
    @Provides
    @Singleton
    fun provideLocationsList() : ArrayList<LatLng> = ArrayList<LatLng>()

    @Provides
    @Singleton
    fun provideNamesList() : ArrayList<String> = ArrayList<String>()

    @Provides
    @Singleton
    fun providePersistService() : PersistService = PersistServiceImpl(provideNamesList(), provideLocationsList())
}