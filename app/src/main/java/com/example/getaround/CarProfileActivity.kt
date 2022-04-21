package com.example.getaround

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.getaround.entities.Car
import java.net.URL
import kotlin.concurrent.thread

class CarProfileActivity : AppCompatActivity() {

    private lateinit var backArrow: ImageView
    private lateinit var carProfilePicture: ImageView
    private lateinit var carName: TextView
    private lateinit var carPrice: TextView
    private lateinit var carRating: RatingBar
    private lateinit var carRatingNumber: TextView
    private lateinit var carOwnerProgressBar: ProgressBar
    private lateinit var carOwnerPicture: ImageView
    private lateinit var carOwnerName: TextView
    private lateinit var carOwnerRating: RatingBar

    private lateinit var car: Car
    private lateinit var appState: GetaroundApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_profile_activity)

        init()

        getViews()
        setViews()
    }

    private fun init() {
        appState = this.application as GetaroundApplication

        val carIndex = intent.getIntExtra(MainActivity.CAR_ID, -1)

        car = try {
            appState.cars?.get(carIndex)!!
        } catch (th: Throwable) {
            // TODO : Show error message to user
            Car()
        }
    }

    private fun getViews() {
        backArrow = findViewById(R.id.car_profile_back_arrow)

        carProfilePicture = findViewById(R.id.car_profile_picture)
        carName = findViewById(R.id.car_profile_name)
        carPrice = findViewById(R.id.car_profile_price)
        carRating = findViewById(R.id.car_profile_rating)
        carRatingNumber = findViewById(R.id.car_profile_rating_number)
        carOwnerProgressBar = findViewById(R.id.car_profile_owner_progress_bar)
        carOwnerPicture = findViewById(R.id.car_profile_owner_picture)
        carOwnerName = findViewById(R.id.car_profile_owner_name)
        carOwnerRating = findViewById(R.id.car_profile_owner_rating)
    }

    private fun setViews() {

        carProfilePicture.setImageBitmap(car.carImage)
        carName.text = car.brand + " " + car.model
        carPrice.text = car.pricePerDay.toString() + "€/j"
        carRating.rating = car.rating.average
        carRatingNumber.text = car.rating.count.toString()
        carOwnerName.text = car.owner.name
        carOwnerRating.rating = car.owner.rating.average

        thread {
            val url = URL(car.owner.picture_url)
            car.owner.ownerImage =
                BitmapFactory.decodeStream(url.openConnection().getInputStream())

            runOnUiThread {
                if (this.window.decorView.rootView.isShown) { // Check if activity is still running
                    carOwnerPicture.setImageBitmap(car.owner.ownerImage)
                    carOwnerPicture.visibility = View.VISIBLE
                    carOwnerProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
}