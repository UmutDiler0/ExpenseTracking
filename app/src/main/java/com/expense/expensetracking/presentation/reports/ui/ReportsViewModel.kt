package com.expense.expensetracking.presentation.reports.ui

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.domain.model.ExpenseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val userDao: UserDao,
) : BaseViewModel<ReportsState, ReportsIntent>(
    initialState = ReportsState()
) {

    init {
        observeData()
    }

    private var allExpenses: List<ExpenseItem> = emptyList()

    public override fun handleIntent(intent: ReportsIntent) {
        when (intent) {
            is ReportsIntent.ChangePeriod -> {
                calculateStats(allExpenses, intent.period)
            }
        }
    }

    private fun observeData() {
        userDao.observeUser()
            .onEach { user ->
                user?.let {
                    allExpenses = it.expenseList
                    calculateStats(allExpenses, uiDataState.value.selectedPeriod)
                }
            }.launchIn(viewModelScope)
    }

    private fun calculateStats(list: List<ExpenseItem>, period: String) {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val filteredList = list.filter { item ->
            val itemCal = Calendar.getInstance().apply { time = item.spendDate.toDate() }
            val itemYear = itemCal.get(Calendar.YEAR)

            when (period) {
                "Günlük" -> {
                    itemYear == currentYear && itemCal.get(Calendar.DAY_OF_YEAR) == today
                }
                "Haftalık" -> {
                    itemYear == currentYear && itemCal.get(Calendar.WEEK_OF_YEAR) == currentWeek
                }
                "Aylık" -> {
                    itemYear == currentYear && itemCal.get(Calendar.MONTH) == currentMonth
                }
                else -> true
            }
        }

        val expensesOnly = filteredList.filter { !it.priceUp }

        handleDataState {
            copy(
                uiState = UiState.Success,
                selectedPeriod = period,
                totalExpense = expensesOnly.sumOf { it.price },
                averageExpense = if (expensesOnly.isNotEmpty()) expensesOnly.sumOf { it.price } / expensesOnly.size else 0,
                categoryDistribution = expensesOnly.groupBy { it.title }.mapValues { it.value.sumOf { e -> e.price } },
                weeklyBarData = getWeeklyBarData(expensesOnly)
            )
        }
    }

    // Private yardımcı fonksiyon olarak ViewModel içinde kalsın
    private fun getWeeklyBarData(list: List<ExpenseItem>): List<Float> {
        val calendar = Calendar.getInstance()
        val weeklyAmounts = MutableList(7) { 0f }
        list.forEach { item ->
            calendar.time = item.spendDate.toDate()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val index = when (dayOfWeek) {
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                Calendar.SUNDAY -> 6
                else -> 0
            }
            weeklyAmounts[index] += item.price.toFloat()
        }
        return weeklyAmounts
    }


}