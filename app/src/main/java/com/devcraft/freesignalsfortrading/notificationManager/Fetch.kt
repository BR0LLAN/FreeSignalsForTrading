package com.devcraft.freesignalsfortrading.notificationManager

import androidx.lifecycle.MutableLiveData
import com.devcraft.freesignalsfortrading.data.ApiConstants
import com.devcraft.freesignalsfortrading.data.dto.GetQuotesListResponse
import com.devcraft.freesignalsfortrading.data.mappers.GetQuotesMapper
import com.devcraft.freesignalsfortrading.data.retrofitApi.QuotesApi
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import com.devcraft.freesignalsfortrading.presentation.ui.common.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fetch {
    private val api: QuotesApi
    var quotesCurrencies: MutableLiveData<MutableList<QuotesCurrency>> = MutableLiveData(
        mutableListOf())
    val getQuotesFailure: SingleLiveEvent<Throwable> = SingleLiveEvent()
    private val mapper: GetQuotesMapper = GetQuotesMapper()

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(ApiConstants.API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        api = retrofit.create(QuotesApi::class.java)
    }

    fun loadData(query: MutableList<String>): MutableLiveData<MutableList<QuotesCurrency>> {
        api.getQuotes(query.joinToString(separator= ","))
            .enqueue(object : Callback<List<GetQuotesListResponse>> {
                override fun onResponse(
                    call: Call<List<GetQuotesListResponse>>,
                    response: Response<List<GetQuotesListResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        quotesCurrencies.postValue(mapper.mapDtoToEntity(response.body()!!))

                    } else {
                        getQuotesFailure.postValue(Error("something get wrong, response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<List<GetQuotesListResponse>>, t: Throwable) {
                    getQuotesFailure.postValue(t)
                }
            })
        return quotesCurrencies
    }
}