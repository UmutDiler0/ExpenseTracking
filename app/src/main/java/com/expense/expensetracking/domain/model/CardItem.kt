package com.expense.expensetracking.domain.model

data class CardItem(
    val name: String = "",
    val balance: Int = 0,
){
    companion object{
        val tempList = listOf(
            CardItem("Ziraat", 450),
            CardItem("HalkBank", 870),
            CardItem("Zasdasd", 250),
            CardItem("Zasd", 550),
            CardItem("Ziraat", 450),
        )
    }
}