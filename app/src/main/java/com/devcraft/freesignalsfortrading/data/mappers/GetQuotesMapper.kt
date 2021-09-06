package com.devcraft.freesignalsfortrading.data.mappers

import android.widget.Toast
import com.devcraft.freesignalsfortrading.data.dto.GetQuotesListResponse
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency

class GetQuotesMapper {
    fun mapDtoToEntity(dto: List<GetQuotesListResponse>): MutableList<QuotesCurrency> {
        return dto.map {
            QuotesCurrency(
                it.symbol,
                it.lasttime,
                it.change,
                it.ask
            )
        }.toMutableList()
    }
}