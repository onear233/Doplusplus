package com.onear.doplusplus.ui.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.onear.doplusplus.AppDestinations
@Composable
fun MainScreen() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TODAY) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { dest ->
                item(
                    icon = { Icon(dest.icon, contentDescription = stringResource(dest.label)) },
                    label = { Text(stringResource(dest.label)) },
                    selected = dest == currentDestination,
                    onClick = { currentDestination = dest }
                )
            }
        }
    ) {
        // 根据当前的导航目标，显示对应的 Screen 组件
        when (currentDestination) {
            AppDestinations.TODAY -> TodayScreen()
            AppDestinations.TODO -> TodoScreen()
            AppDestinations.PROFILE -> ProfileScreen()
        }
    }
}
