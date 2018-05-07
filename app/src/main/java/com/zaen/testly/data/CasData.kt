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
}