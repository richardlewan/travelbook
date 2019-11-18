package com.richardlewan.travelbook.service

import android.content.Context
import com.google.android.gms.maps.model.LatLng

interface PersistService {
    fun fetchPlaces(context: Context, namesList: ArrayList<String>, locationsList: ArrayList<LatLng>)
    fun savePlace(context: Context, namesList: ArrayList<String>, locationsList: ArrayList<LatLng>,
                  address: String, latitude: Double, longitude: Double)
}