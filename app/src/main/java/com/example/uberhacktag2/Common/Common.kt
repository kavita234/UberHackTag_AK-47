package com.example.uberhacktag2.Common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.uberhacktag2.Model.DriverGeoModel
import com.example.uberhacktag2.Model.RiderModel
import com.example.uberhacktag2.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


object Common
{

    fun buildWelcomeMessage(): String {
        return StringBuilder("Welcome, ")
            .append(currentRider!!.firstName)
            .append(" ")
            .append(currentRider!!.lastName)
            .toString()
    }

    fun showNotification(context: Context, id: Int, title: String?, body: String?, intent: Intent?) {
        var pendingIntent: PendingIntent? = null
        if(intent != null)
        {
            pendingIntent = PendingIntent.getActivity(context,id,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val NOTIFICATION_CHANNEL_ID= "edmt_dev_uber_make"
        val notificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel= NotificationChannel(NOTIFICATION_CHANNEL_ID,"User Remake", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Uber Remake"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0,1000,500,1000)
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder= NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.ic_baseline_directions_car_24))

        if(pendingIntent != null)
        {
            builder.setContentIntent(pendingIntent)

        }

        val notification = builder.build()
        notificationManager.notify(id, notification)
    }

    fun buildName(firstName: String, lastName: String): String? {
        return StringBuilder(firstName).append(" ").append(lastName).toString()
    }

    val MarkerList: MutableMap<String,Marker> = HashMap<String, Marker>()
    val DRIVER_INFO_REFERENCES: String= "DriverInfo"
    val driversFound: MutableSet<DriverGeoModel> =  HashSet<DriverGeoModel>()
    val DRIVER_LOCATION_REFERENCES: String= "DriversLocation"
    val NOTI_BODY: String = "body"
    val NOTI_TITLE: String="title"

    val TOKEN_REFERENCE: String= "Token"
    var currentRider: RiderModel? = null
    val RIDER_INFO_REFERENCE: String = "Riders"

    //GET BEARING
    fun getBearing(begin: LatLng, end: LatLng): Float {
        //You can copy this function by link at description
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)
        if (begin.latitude < end.latitude && begin.longitude < end.longitude) return Math.toDegrees(
            Math.atan(lng / lat)
        )
            .toFloat() else if (begin.latitude >= end.latitude && begin.longitude < end.longitude) return (90 - Math.toDegrees(
            Math.atan(lng / lat)
        ) + 90).toFloat() else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude) return (Math.toDegrees(
            Math.atan(lng / lat)
        ) + 180).toFloat() else if (begin.latitude < end.latitude && begin.longitude >= end.longitude) return (90 - Math.toDegrees(
            Math.atan(lng / lat)
        ) + 270).toFloat()
        return (-1).toFloat()
    }

    //DECODE POLY
    fun decodePoly(encoded: String): ArrayList<Any>? {
        val poly= ArrayList<Any>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}