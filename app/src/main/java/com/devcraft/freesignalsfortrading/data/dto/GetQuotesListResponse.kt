package com.devcraft.freesignalsfortrading.data.dto


data class GetQuotesListResponse(
        val ask: Double,
        val change: Double,
        val symbol: String,
        val lasttime: Long
)