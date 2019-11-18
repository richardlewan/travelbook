package com.richardlewan.travelbook.dagger

import com.richardlewan.travelbook.MainActivity
import com.richardlewan.travelbook.MapsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, LocationsModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: MapsActivity)
}