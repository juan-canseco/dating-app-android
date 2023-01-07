package com.org.datingapp.core.domain.user.details

import java.io.Serializable

data class BirthDate(
    val year : Int,
    val month : Int,
    val day : Int) : Serializable {
        constructor() : this(0, 0,0)

        companion object {
            fun empty() : BirthDate {
                return BirthDate()
            }
        }

    }

