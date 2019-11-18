package com.richardlewan.travelbook.service

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class PersistServiceImpl : PersistService {

    var locationsList: ArrayList<LatLng>

    var namesList: ArrayList<String>

    @Inject
    constructor(namesList: ArrayList<String>, locationsList: ArrayList<LatLng>) {
        this.namesList = namesList
        this.locationsList = locationsList
    }

    override fun fetchPlaces(context: Context) {
        try {
            val database = context.openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM places", null)

            val nameIndex = cursor.getColumnIndex("name")
            val latitudeIndex = cursor.getColumnIndex("latitude")
            val longitudeIndex = cursor.getColumnIndex("longitude")
            cursor.moveToFirst()

            namesList.clear()
            locationsList.clear()

            while (!cursor.isAfterLast) {
                val nameFromDatabase = cursor.getString(nameIndex)
                val latitudeFromDatabase = cursor.getString(latitudeIndex)
                val longitudeFromDatabase = cursor.getString(longitudeIndex)
                namesList.add(nameFromDatabase)

                val latitudeCoordinate = latitudeFromDatabase.toDouble()
                val longitudeCoordinate = longitudeFromDatabase.toDouble()

                val location = LatLng(latitudeCoordinate, longitudeCoordinate)
                locationsList.add(location)

                cursor.moveToNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun savePlace(context: Context, address: String, latitude: Double, longitude: Double) {
        try {
            val lat = latitude.toString()
            val long = longitude.toString()

            val database = context.openOrCreateDatabase("Places", Context.MODE_PRIVATE,null)

            database.execSQL("CREATE TABLE IF NOT EXISTS places (name VARCHAR, latitude VARCHAR, longitude VARCHAR)")

            val toCompile = "INSERT INTO places (name, latitude, longitude) VALUES (?, ?, ?)"

            val sqLiteStatement = database.compileStatement(toCompile)

            sqLiteStatement.bindString(1,address)
            sqLiteStatement.bindString(2,lat)
            sqLiteStatement.bindString(3,long)

            sqLiteStatement.execute()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}