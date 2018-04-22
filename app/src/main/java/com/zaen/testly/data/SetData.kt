package com.zaen.testly.data

data class SetData(
        val id: String,
        // Unique document id

        val type: String,
        // check, test

        val cardType: String,
        // mixed, vocabulary, spelling

        val subjectType: String,
        // mixed, english, math, japanese...

        val cards: ArrayList<String>
        // List of cards to look for
)