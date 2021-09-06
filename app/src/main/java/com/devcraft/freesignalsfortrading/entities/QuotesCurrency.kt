package com.devcraft.freesignalsfortrading.entities

data class QuotesCurrency(
    var symbol: String,
    var datetime: Long,
    var percent_change: Double,
    var close: Double
)