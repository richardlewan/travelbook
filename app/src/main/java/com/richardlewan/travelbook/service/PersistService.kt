package com.richardlewan.travelbook.service

import android.content.Context

interface PersistService {
    fun fetchPlaces(context: Context)
    fun savePlace(context: Context, address: String, latitude: Double, longitude: Double)
}