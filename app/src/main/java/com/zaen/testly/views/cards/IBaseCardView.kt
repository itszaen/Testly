package com.zaen.testly.views.cards

interface IBaseCardView {
    var hasInflated: Boolean

    fun showAnswer()
    fun animateAnswer()
}