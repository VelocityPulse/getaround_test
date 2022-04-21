package com.example.getaround.adapters

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getaround.R
import com.example.getaround.entities.Car
import java.net.URL
import kotlin.concurrent.thread

class MainRecyclerAdapter(val mCars: Array<Car>, val mContext: Activity) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.view_holder_car, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = mCars[position]

        holder.car = car

        if (car.carImage == null) {
            thread {
                try {
                    holder.carPicture.visibility = View.GONE
                    val url = URL(car.pictureUrl)
                    car.carImage =
                        BitmapFactory.decodeStream(url.openConnection().getInputStream())

                    if (holder.car == car) {
                        mContext.runOnUiThread {
                            holder.carPicture.setImageBitmap(car.carImage)
                            holder.carPicture.visibility = View.VISIBLE
                        }
                    }
                } catch (th: Throwable) {
                    th.printStackTrace()
                }
            }
        } else {
            holder.carPicture.setImageBitmap(car.carImage)
            holder.carPicture.visibility = View.VISIBLE
        }

        holder.carName.text = car.brand + " " + car.model
        holder.carPrice.text = car.pricePerDay.toString() + "€/j"
        holder.carRating.rating = car.rating.average
        holder.carRatingNumber.text = "(" + car.rating.count + ")"

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(holder.car)
        }
    }

    override fun getItemCount(): Int {
        return mCars.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var car: Car
        val carPicture: ImageView
        val carName: TextView
        val carPrice: TextView
        val carRating: RatingBar
        val carRatingNumber: TextView

        init {
            carPicture = itemView.findViewById(R.id.vh_car_picture)
            carName = itemView.findViewById(R.id.vh_car_name)
            carPrice = itemView.findViewById(R.id.vh_car_price)
            carRating = itemView.findViewById(R.id.vh_car_rating)
            carRatingNumber = itemView.findViewById(R.id.vh_car_rating_number)
        }
    }

    interface OnClickListener {
        fun onClick(car: Car)
    }

}