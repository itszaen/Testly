package com.zaen.testly.data

open class CasData(
        open var id: String,
        open var timestamp: Long
) {
    companion object {
        const val card = "card"
        const val set = "set"
    }
    open var casType: String = ""
    // card - "card", set - "set"

//    var id: String = ""
//    // Unique document id
//
//    var timestamp: Long = 0L
//    // Unix timestamp when created

//    fun CasData(id: String, timestamp: Long){
//        this.id = id
//        this.timestamp = timestamp
//    }
}