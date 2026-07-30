package com.onear.doplusplus.ui.screen.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class OnboardingPageData(
    val title: String,
    val description: String
)


val pages = listOf(
    OnboardingPageData("欢迎使用1", "1"),
    OnboardingPageData("欢迎使用2", "2"),
    OnboardingPageData("欢迎使用3", "3")
)


@Composable
fun TutorialPager() {
    val pagerState = rememberPagerState() { pages.size }

    HorizontalPager(state = pagerState) { pageIndex ->
        OnboardingPageContent(pageData = pages[pageIndex])
    }
}

@Composable
fun OnboardingPageContent(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = pageData.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = pageData.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun PreviewTutorialPager() {
    TutorialPager()
}