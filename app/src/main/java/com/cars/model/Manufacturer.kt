package com.cars.model

import android.os.Parcel
import android.os.Parcelable

class Manufacturer(private val id: String, private val name: String) : DataModel {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
    }

    override fun describeContents(): Int = 0

    override fun getDataId(): String = id

    override fun getDisplayData(): String = name

    companion object CREATOR : Parcelable.Creator<Manufacturer> {
        override fun createFromParcel(parcel: Parcel): Manufacturer {
            return Manufacturer(parcel)
        }

        override fun newArray(size: Int): Array<Manufacturer?> {
            return arrayOfNulls(size)
        }
    }
}
