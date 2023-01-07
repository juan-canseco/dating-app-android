package com.org.datingapp.core.domain.user.details

import java.io.Serializable

data class Gender(val id : Int = 0, val name : String = "") : Serializable {

    constructor() : this(0,"")

    companion object {
        fun empty() : Gender {
            return Gender()
        }
    }

}