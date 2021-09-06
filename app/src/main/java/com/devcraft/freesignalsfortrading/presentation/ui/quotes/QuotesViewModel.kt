package com.devcraft.freesignalsfortrading.presentation.ui.quotes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devcraft.freesignalsfortrading.data.dto.GetQuotesListResponse
import com.devcraft.freesignalsfortrading.data.mappers.GetQuotesMapper
import com.devcraft.freesignalsfortrading.data.retrofitApi.QuotesApi
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import com.devcraft.freesignalsfortrading.presentation.ui.common.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuotesViewModel(
    private val quotesApi: QuotesApi,
    private val getQuotesMapper: GetQuotesMapper
) : ViewModel()

{
    val quotesCurrencies: MutableLiveData<MutableList<QuotesCurrency>> = MutableLiveData(
        mutableListOf())

    val getQuotesFailure: SingleLiveEvent<Throwable> = SingleLiveEvent()

    fun loadData(query: MutableList<String>){
        quotesApi.getQuotes(query.joinToString(separator= ","))
            .enqueue(object : Callback<List<GetQuotesListResponse>>{
                override fun onResponse(
                    call: Call<List<GetQuotesListResponse>>,
                    response: Response<List<GetQuotesListResponse>>
                ) {

                    if (response.isSuccessful && response.body() != null) {
                        quotesCurrencies.postValue(getQuotesMapper.mapDtoToEntity(response.body()!!))
                    } else {
                        getQuotesFailure.postValue(Error("something get wrong, response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<List<GetQuotesListResponse>>, t: Throwable) {
                    getQuotesFailure.postValue(t)
                }
            })
    }

}
