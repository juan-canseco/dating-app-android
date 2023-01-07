package com.org.datingapp.core.domain.user.details


import java.io.Serializable

data class Activity(val id : String = "", val name : String = "") : Serializable {
    constructor() : this("", "")
    companion object {
        fun empty() : Activity {
            return Activity()
        }
    }
}