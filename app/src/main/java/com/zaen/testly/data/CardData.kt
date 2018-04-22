package com.zaen.testly.data


data class VocabularyCardData(
        val id: String,
        // Unique document id

        val language: String,
        // unsorted, english, japanese, chinese

        val type: String,
        // unsorted, *vocabulary, spelling,

        val subject: String,
        // unsorted, english, math, japanese...

        val hasAnswerCard: Boolean,
        // Whether to have a separate answer card

        val question: String,

        val options: ArrayList<String>,

        val answer: String,

        val returnType: Any?
        // boolean?

)
data class SpellingCardData(
        val id: String,
        // Unique document id

        val type: String,
        // unsorted, vocabulary, *spelling,

        val subject: String,
        // unsorted, english, math, japanese...

        val hasAnswerCard: Boolean,
        // Whether to have a separate answer card

        val question: String,

        val mask: ArrayList<Int>,

        val answer: String,

        val returnType: Any?

)