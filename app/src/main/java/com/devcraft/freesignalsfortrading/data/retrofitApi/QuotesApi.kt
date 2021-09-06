package com.devcraft.freesignalsfortrading.data.retrofitApi

import com.devcraft.freesignalsfortrading.data.ApiConstants
import com.devcraft.freesignalsfortrading.data.dto.GetQuotesListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {
    @GET(ApiConstants.API_ENDPOINT_LATEST_CURRENCY)
    fun getQuotes(@Query("q") symbol: String): Call<List<GetQuotesListResponse>>

}