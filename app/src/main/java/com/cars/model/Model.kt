package com.cars.model

import android.os.Parcel
import android.os.Parcelable

class Model(private val model: String) : DataModel {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(model)
    }

    override fun describeContents(): Int = 0

    override fun getDataId(): String = model

    override fun getDisplayData(): String = model

    companion object CREATOR : Parcelable.Creator<Model> {
        override fun createFromParcel(parcel: Parcel): Model {
            return Model(parcel)
        }

        override fun newArray(size: Int): Array<Model?> {
            return arrayOfNulls(size)
        }
    }
}
