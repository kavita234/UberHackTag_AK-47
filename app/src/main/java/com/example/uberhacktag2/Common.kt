package com.example.uberhacktag2

import com.example.uberhacktag2.Model.DriverInfoModel
import java.lang.StringBuilder

object Common2 {
    fun buildWelcomeMessage(): String {
        return StringBuilder("Welcome, ")
            .append(currentUser!!.firstName)
            .append(" ")
            .append(currentUser!!.lastName)
            .toString()
    }


    val DRIVERS_LOCATION_REFERENCES: String = "DriversLocation"
    var currentUser: DriverInfoModel? = null
    const val DRIVER_INFO_REFERENCE:String= "DriverInfo"
}