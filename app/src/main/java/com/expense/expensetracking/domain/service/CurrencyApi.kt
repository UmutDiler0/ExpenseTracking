package com.expense.expensetracking.domain.service

import com.expense.expensetracking.domain.model.CountryDto
import com.expense.expensetracking.domain.model.RatesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("v1/countries.json")
    suspend fun getCountries(): Map<String, CountryDto>

    // Tüm currency code + isim listesi
    @GET("v1/currencies.json")
    suspend fun getCurrencyList(): Map<String, String>

    // Base currency -> diğer tüm kurlar
    @GET("v1/currencies/{base}.json")
    suspend fun getRatesByBase(
        @Path("base") base: String
    ): RatesResponse
}