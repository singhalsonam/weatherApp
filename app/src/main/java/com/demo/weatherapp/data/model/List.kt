package com.demo.weatherapp.data.model

import com.google.gson.annotations.SerializedName


data class List (

  @SerializedName("dt"         ) var dt         : Int?               = null,
  @SerializedName("main"       ) var main       : Main?              = Main(),
  @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
  @SerializedName("visibility" ) var visibility : Int?               = null,
  @SerializedName("pop"        ) var pop        : Double?            = null,
  @SerializedName("dt_txt"     ) var dtTxt      : String?            = null

)