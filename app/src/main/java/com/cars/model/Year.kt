package com.cars.model

import android.os.Parcel
import android.os.Parcelable

class Year(private val year: String) : DataModel {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(year)
    }

    override fun describeContents(): Int = 0

    override fun getDataId(): String = year

    override fun getDisplayData(): String = year

    companion object CREATOR : Parcelable.Creator<Year> {
        override fun createFromParcel(parcel: Parcel): Year {
            return Year(parcel)
        }

        override fun newArray(size: Int): Array<Year?> {
            return arrayOfNulls(size)
        }
    }
}
