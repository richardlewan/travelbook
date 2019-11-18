package com.richardlewan.travelbook

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.richardlewan.travelbook.service.PersistServiceImpl

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.richardlewan.travelbook", appContext.packageName)
    }

    @Test
    fun testPersistService() {
        // Given
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val address = "123 Main"
        val lat = 123.321
        val long = -432.312
        val location = LatLng(lat, long)
        val persistService = PersistServiceImpl(ArrayList(), ArrayList())

        // When
        persistService.savePlace(appContext, address, lat, long)

        // Then
        persistService.fetchPlaces(appContext)
        assert(address in persistService.namesList)
        assert(location in persistService.locationsList)
    }
}
