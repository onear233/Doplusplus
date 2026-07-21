package com.onear.doplusplus.ui.screen.main

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onear.doplusplus.AppDestinations
import com.onear.doplusplus.data.TodoRepository
import com.onear.doplusplus.data.local.AppDatabase
import com.onear.doplusplus.viewmodel.ProfileViewModel
import com.onear.doplusplus.viewmodel.TodoViewModel


@Composable
fun MainScreen(onNavigate: (String) -> Unit = {}) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TODAY) }

    val currentContext = LocalContext.current;

    val database = AppDatabase.getDatabase(context = currentContext)

    //组装 Repository (把 Dao 传进去)
    //repository的作用是给viewmodel数据；viewmodel不关心数据是怎么来的，repo看起来只是调用了dao，事实上随着项目功能变多repo的逻辑会变得多样化
    val repository = TodoRepository(database.todoDao(), database.filterDao())

    //自定义一个工厂，告诉系统怎么创建带参数的TodoViewModel
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
                return TodoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    //利用工厂获取 ViewModel 实例
    val todoViewModel: TodoViewModel = viewModel(factory = viewModelFactory)
    val profileViewModel: ProfileViewModel = viewModel()

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
        //根据当前的导航目标，显示对应的Screen组件
        when (currentDestination) {
            AppDestinations.TODAY -> TodayScreen()
            AppDestinations.TODO -> TodoScreen(viewModel = todoViewModel)
            AppDestinations.PROFILE -> ProfileScreen(
                viewModel = profileViewModel,
                onNavigate = onNavigate
            )
        }
    }
}
