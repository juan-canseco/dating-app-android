package com.org.datingapp.core.domain.user.details

import java.io.Serializable

data class Interest(val id : String = "", val name : String = "") : Serializable {
    constructor() : this("", "")
    companion object {
        fun empty() : Interest {
            return Interest()
        }
    }
}