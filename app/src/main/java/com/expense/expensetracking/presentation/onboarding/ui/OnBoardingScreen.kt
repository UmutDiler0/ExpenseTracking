package com.expense.expensetracking.presentation.onboarding.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.presentation.onboarding.component.PagerItem
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onNavigateHomeScreen: () -> Unit
){
    val scope = rememberCoroutineScope() // Butonla sayfa geçişi için
    val pages = listOf(
        OnboardingPageData(
            imageSource = R.drawable.credit_cards,
            title = "Harcamalarını Takip Et",
            description = "Nereye ne kadar harcadığını kolayca gör."
        ),
        OnboardingPageData(
            imageSource = R.drawable.financial_protection,
            title = "Tasarruf Et",
            description = "Ne kadar harcadığını gör ve tasarruf et."
        ),
        OnboardingPageData(
            imageSource = R.drawable.finance_management,
            title = "Harcamalarını Takip Et",
            description = "Nereye ne kadar harcadığını kolayca gör."
        ),

    )
    val pagerState = rememberPagerState(pageCount = { pages.size })



        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index

                    val color = if (isSelected) {
                        PrimaryGreen
                    } else {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    }

                    val animatedColor by animateColorAsState(targetValue = color, label = "color")

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(animatedColor)
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                PagerItem(
                    image = pages[pageIndex].imageSource,
                    label = pages[pageIndex].title,
                    desc = pages[pageIndex].description
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick ={
                        viewModel.completeOnboarding()
                        onNavigateHomeScreen()
                    } ) {
                        Text(
                            "Atla",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("İleri", color = MaterialTheme.colorScheme.onPrimary)
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.completeOnboarding()
                            onNavigateHomeScreen()
                                  },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Hemen Başla", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

}

data class OnboardingPageData(
    val imageSource: Int,
    val title: String,
    val description: String // İstersen açıklama da ekleyebilirsin
)