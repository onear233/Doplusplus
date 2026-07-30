package com.onear.doplusplus.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onear.doplusplus.R
import com.onear.doplusplus.data.TodoRepository
import com.onear.doplusplus.data.local.FilterDao
import com.onear.doplusplus.data.local.TodoDao
import com.onear.doplusplus.ui.screen.main.arrangement.ScheduleScreen
import com.onear.doplusplus.ui.screen.main.arrangement.TodoScreen
import com.onear.doplusplus.viewmodel.TodoViewModel

lateinit var todoViewModel : TodoViewModel
@Composable
fun ArrangementScreen(viewModel: TodoViewModel) {
    todoViewModel = viewModel
    SecondaryTabs()
}

enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    SCHEDULE("schedule", "Schedule", "Schedule"),

    TODOLIST("todolist", "TodoList", "TodoList"),
}

@Preview
@Composable
fun PreviewSecondaryTabs() {
    SecondaryTabs()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTabs() {
    //创建导航器
    val navController = rememberNavController()
    //定义默认地址
    val startDestination = Destination.SCHEDULE
    //定义选择的地址
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.arr_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            PrimaryTabRow(
                selectedTabIndex = selectedDestination,
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            AppNavHost(navController, startDestination)
        }

    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.TODOLIST -> TodoScreen(Modifier, todoViewModel)
                    Destination.SCHEDULE -> ScheduleScreen()
                }
            }
        }
    }
}

