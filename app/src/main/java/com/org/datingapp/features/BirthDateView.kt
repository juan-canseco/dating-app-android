package com.org.datingapp.features

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.org.datingapp.core.domain.user.details.BirthDate

@Keep
data class BirthDateView(val year : Int, val month : Int, val day : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BirthDateView> {
        override fun createFromParcel(parcel: Parcel): BirthDateView {
            return BirthDateView(parcel)
        }

        override fun newArray(size: Int): Array<BirthDateView?> {
            return arrayOfNulls(size)
        }
    }

    fun toBirthDate() = BirthDate(year, month, day)
}