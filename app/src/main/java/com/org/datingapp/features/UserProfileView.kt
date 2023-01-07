package com.org.datingapp.features

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
class UserProfileView (val fullName : String,
                       val birthDate : BirthDateView,
                       val gender : String,
                       val username : String,
                       val description : String?,
                       val interests : ArrayList<String>,
                       val pictureUris : ArrayList<String>) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(BirthDateView::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeParcelable(birthDate, flags)
        parcel.writeString(gender)
        parcel.writeString(username)
        parcel.writeString(description)
        parcel.writeStringList(interests)
        parcel.writeStringList(pictureUris)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfileView> {
        override fun createFromParcel(parcel: Parcel): UserProfileView {
            return UserProfileView(parcel)
        }

        override fun newArray(size: Int): Array<UserProfileView?> {
            return arrayOfNulls(size)
        }
    }
}