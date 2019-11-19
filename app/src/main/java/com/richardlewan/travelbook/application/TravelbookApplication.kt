package com.richardlewan.travelbook.application

import android.app.Application
import com.richardlewan.travelbook.dagger.AppComponent
import com.richardlewan.travelbook.dagger.AppModule
import com.richardlewan.travelbook.dagger.DaggerAppComponent

class TravelbookApplication : Application() {

    lateinit var travelbookComponent: AppComponent

    private fun initDagger(app: TravelbookApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    override fun onCreate() {
        super.onCreate()
        travelbookComponent = initDagger(this)
    }
}