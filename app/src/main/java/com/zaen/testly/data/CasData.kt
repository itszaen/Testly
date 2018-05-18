package com.zaen.testly.data

open class CasData(
        open var id: String,
        open var timestamp: Long
) {
    companion object {
        const val CARD = "card"
        const val SET = "set"
    }
    open var casType: String = ""
}