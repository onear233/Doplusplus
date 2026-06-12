package com.onear.doplusplus.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onear.doplusplus.Greeting
import com.onear.doplusplus.viewmodel.TodayViewModel


@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = viewModel()
) {
    val greetingText by viewModel.greeting.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateGreeting()
    }

    Greeting(
        content = stringResource(greetingText),
        modifier = modifier.padding(30.dp, 40.dp)
    )
}

