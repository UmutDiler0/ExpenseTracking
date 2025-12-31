package com.expense.expensetracking.domain.model

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("country_name")
    val countryName: String,

    @SerializedName("country_iso3")
    val countryIso3: String,

    @SerializedName("country_iso_numeric")
    val countryIsoNumeric: String,

    @SerializedName("currency_name")
    val currencyName: String,

    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("currency_number")
    val currencyNumber: String
)
