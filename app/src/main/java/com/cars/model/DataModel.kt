package com.cars.model

import android.os.Parcelable

interface DataModel : Parcelable {
    fun getDataId(): String
    fun getDisplayData(): String
}