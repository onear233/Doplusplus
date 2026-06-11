package com.onear.doplusplus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onear.doplusplus.ui.data.TodoRepository
import com.onear.doplusplus.ui.data.entity.TodoTask
import com.onear.doplusplus.viewmodel.TodayViewModel
import com.onear.doplusplus.viewmodel.TodoViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel()
) {
    /*
    Learn in USE:
    Scaffold 可组合项提供了一个简单的 API，您可以使用该 API 根据 Material Design 准则快速组装应用结构。Scaffold 接受多个可组合项作为参数。其中包括以下各项：
    topBar：屏幕顶部的应用栏。
    bottomBar：屏幕底部的应用栏。
    floatingActionButton：悬浮在屏幕右下角的按钮，可以使用该按钮来显示关键操作。
    * */
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    //有点像WPF里的binding
    //界面刷新是数据驱动的，而不是直接操作
    val todoList by viewModel.todoListState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            BottomAppBar(
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        value = inputText,
                        onValueChange = { newText: String ->
                            inputText = newText
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (!inputText.isEmpty()) {
                                viewModel.addTask(inputText)
                                inputText = ""

                            }
                        },
                        Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Add Task"
                        )
                    }
                }

            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {

                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "SortByDate"
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }

                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            FilterChip(ChipType.ADD)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = todoList, key = { it.taskID }) { task ->
//                    val dismissState = rememberSwipeToDismissBoxState()
//                    //LIU: LaunchedEffect是一个能让我们在compose里执行异步任务的工具
//                    //如果直接在composable里写这种任务，会导致界面一旦重组刷新，该请求就会被反复执行
//                    //它的生命周期紧紧的跟随组建的生命周期
//                    //传入key：决定何时重新执行，即：key不变化，不会重复执行
//                    //在这个地方是，如果targetValue变成了EndToStart就执行delete
//                    LaunchedEffect(dismissState.targetValue) {
//                        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
//                            kotlinx.coroutines.delay(200) //给动画留 200ms 的飞出时间
//                            viewModel.deleteTask(task)
//                        }
//                    }
                    //https://developer.android.google.cn/develop/ui/compose/touch-input/user-interactions/swipe-to-dismiss?hl=zh-cn

                    ListCard(
                        task = task,
                        onCheckedChange = { viewModel.completeTask(task) },
                        onRemove = { viewModel.deleteTask(task) }
                    )
                }

            }
        }
    }


}


@Composable
fun ListCard(
    task: TodoTask,
    onCheckedChange: (Boolean) -> Unit, //回调方法，即：ListCard不可以对数据做任何操作，要由上一级完成
    onRemove: (TodoTask) -> Unit, //回调方法，处理右滑删除
    modifier: Modifier = Modifier
) {

    val dismissState = rememberSwipeToDismissBoxState()

    val scope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                scope.launch {
                        delay(200)
                        onRemove(task)
                }


            }
            // Reset item when toggling done status
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = Modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }

                SwipeToDismissBoxValue.Settled -> {}
                else -> {}
            }
        }

    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { isChecked ->
                        onCheckedChange(isChecked)
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.taskText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "创建于: ${formatTime(task.taskCreateDate)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun FilterChip(chipType: ChipType) {
    var selected by remember { mutableStateOf(false) }

//    FilterChip(
//        onClick = { if (chipType == ChipType.ADD) else selected = !selected },
//        label = {
//            Text("Filter chip")
//        },
//        selected = selected,
//        leadingIcon = if (selected) {
//            {
//                Icon(
//                    imageVector = Icons.Filled.Done,
//                    contentDescription = "Done icon",
//                    modifier = Modifier.size(FilterChipDefaults.IconSize)
//                )
//            }
//        } else {
//            null
//        },
//    )
}

enum class ChipType() {
    ADD, FILTER
}

@Preview(showBackground = true, name = "正常状态Light")
@Composable
fun ListCardPreview() {
    com.onear.doplusplus.ui.theme.DoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "未完成状态",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            ListCard(
                task = TodoTask(
                    taskID = 1,
                    taskText = "Task to be done",
                    taskDueDate = null,
                    taskCreateDate = System.currentTimeMillis(),
                    isCompleted = false
                ),
                onCheckedChange = {}, onRemove = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "已完成状态",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            ListCard(
                task = TodoTask(
                    taskID = 2,
                    taskText = "Task has been completed",
                    taskDueDate = null,
                    taskCreateDate = System.currentTimeMillis() - 3600000, // 一小时前
                    isCompleted = true
                ),
                onCheckedChange = { }, onRemove = {}
            )
        }
    }
}