package com.zaen.testly.data

open class CasData {
    var id: String = ""
    // Unique document id

    var timestamp: Long = 0L
    // Unix timestamp when created

    fun CasData(id: String, timestamp: Long){
        this.id = id
        this.timestamp = timestamp
    }
}