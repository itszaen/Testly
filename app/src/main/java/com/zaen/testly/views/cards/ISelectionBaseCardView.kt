package com.zaen.testly.views.cards

interface ISelectionBaseCardView {
    fun inflate(options: ArrayList<String>)

    fun disableOptions()
}