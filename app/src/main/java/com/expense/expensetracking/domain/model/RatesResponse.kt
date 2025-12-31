package com.expense.expensetracking.domain.model

import com.google.gson.annotations.SerializedName

data class RatesResponse(
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
)
