package com.example.getaround

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getaround.adapters.MainRecyclerAdapter
import com.example.getaround.entities.Car
import com.example.getaround.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {


    private val CARS_JSON_URL =
        "https://raw.githubusercontent.com/drivy/jobs/master/mobile/api/cars.json"

    private lateinit var appState: GetaroundApplication

    private var adapter: MainRecyclerAdapter? = null

    companion object {
        val CAR_ID: String = "CAR_ID_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        appState = this.application as GetaroundApplication

        runBlocking {
            val cars: Array<Car>? = fetchCars()

            if (cars != null) {
                appState.cars = cars
                createAdapter(cars)
            }
            // TODO try another reload if the fetch fails
        }
    }

    private suspend fun fetchCars(): Array<Car>? {
        return try {
            val jsonCars = Utils.httpGet(CARS_JSON_URL)
            Gson().fromJson(jsonCars, Array<Car>::class.java)
        } catch (th: Throwable) {
            th.printStackTrace()
            null
        }
    }

    private fun createAdapter(carsArray: Array<Car>) {
        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MainRecyclerAdapter(carsArray, this)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter!!.onClickListener = object : MainRecyclerAdapter.OnClickListener {
            override fun onClick(car: Car) {
                println("CLICK")
                prepareToStartCarProfileActivity(car)
            }
        }
    }

    private fun prepareToStartCarProfileActivity(car: Car) {

        val intent = Intent(this, CarProfileActivity::class.java).apply {
            appState.cars?.let { putExtra(CAR_ID, it.indexOf(car)) }
        }

        startActivity(intent)
    }
}