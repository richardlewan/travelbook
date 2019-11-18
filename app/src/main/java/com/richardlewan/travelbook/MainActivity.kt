package com.richardlewan.travelbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import com.richardlewan.travelbook.application.TravelbookApplication
import com.richardlewan.travelbook.service.PersistService
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var locationsList: ArrayList<LatLng>

    @Inject
    lateinit var namesList: ArrayList<String>

    @Inject
    lateinit var persistService: PersistService

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_place, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == R.id.add_place) {

            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("info", "new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        // Fetch the existing Places from the sqlLite DB.
        persistService.fetchPlaces(applicationContext)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, namesList)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { adapterView, view, i, l ->

            val intent = Intent(applicationContext,MapsActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("name", namesList[i])
            intent.putExtra("latitude", locationsList[i].latitude)
            intent.putExtra("longitude", locationsList[i].longitude)
            startActivity(intent)
        }

        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as TravelbookApplication).travelbookComponent.inject(this)
    }
}
