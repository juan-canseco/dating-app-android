package com.org.datingapp.features.onboarding

import com.org.datingapp.core.domain.user.User

interface SessionManager {

    fun getUser() : User
    fun updateUser()

    class Implementation {

    }
}