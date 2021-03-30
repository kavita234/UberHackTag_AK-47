package com.example.uberhacktag2.Callback

import com.example.uberhacktag2.Model.DriverGeoModel

interface FirebaseDriverInfoListener {
    fun onDriverInfoLoadSuccess(driverGeoModel: DriverGeoModel?)
}