package com.org.datingapp.core.domain.contactrequest

data class ContactRequest(
    val id :  String,
    val fromUserId : String,
    val toUserId : String,
    val status : ContactRequestStatus,
    val timestamp : Long)