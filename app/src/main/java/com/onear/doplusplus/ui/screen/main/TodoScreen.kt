package com.onear.doplusplus.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onear.doplusplus.R
import com.onear.doplusplus.data.entity.FilterTag
import com.onear.doplusplus.data.entity.TodoTask
import com.onear.doplusplus.viewmodel.TodoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

private val filterPresetColors = listOf(
    0xFF6750A4L to "紫",
    0xFFE91E63L to "粉",
    0xFF2196F3L to "蓝",
    0xFF4CAF50L to "绿",
    0xFFFF9800L to "橙",
    0xFFF44336L to "红",
    0xFF009688L to "青"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val todoList by viewModel.todoListState.collectAsState()
    val filterList by viewModel.filterListState.collectAsState()
    val selectedFilter by viewModel.selectedFilterState.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var editingTask by remember { mutableStateOf<TodoTask?>(null) }
    var showCreateSheet by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val createSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.todo_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(stringResource(R.string.quick_add_hint)) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { showCreateSheet = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "展开创建",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            val text = inputText.trim()
                            if (text.isNotEmpty()) {
                                viewModel.addTask(
                                    title = text,
                                    filterTag = selectedFilter
                                )
                                inputText = ""
                            }
                        },
                        modifier = Modifier.size(40.dp),
                        enabled = inputText.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "快速添加",
                            tint = if (inputText.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            FilterChipsRow(
                filters = filterList,
                selectedFilter = selectedFilter,
                onFilterClick = { tag ->
                    viewModel.selectFilter(tag)
                },
                onAddFilterClick = { showFilterDialog = true },
                onDeleteFilter = { filter ->
                    viewModel.deleteFilter(filter)
                }
            )

            if (todoList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_tasks),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = todoList, key = { it.taskID }) { task ->
                        ListCard(
                            task = task,
                            onCheckedChange = { viewModel.completeTask(task) },
                            onRemove = { viewModel.deleteTask(task) },
                            onClick = { editingTask = task }
                        )
                    }
                }
            }
        }
    }

    if (editingTask != null) {
        val task = editingTask!!
        var editText by remember(task.taskID) { mutableStateOf(task.taskText) }
        var editDueDate by remember(task.taskID) { mutableStateOf(task.taskDueDate) }
        var editTag by remember(task.taskID) { mutableStateOf(task.filterTag) }
        var showDatePicker by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { editingTask = null },
            sheetState = editSheetState
        ) {
            TaskEditSheetContent(
                title = stringResource(R.string.edit_task_title),
                taskText = editText,
                onTaskTextChange = { editText = it },
                dueDate = editDueDate,
                onDueDateChange = { editDueDate = it },
                filterTag = editTag,
                onFilterTagChange = { editTag = it },
                filters = filterList,
                showDatePicker = showDatePicker,
                onShowDatePickerChange = { showDatePicker = it },
                onSave = {
                    if (editText.isNotBlank()) {
                        viewModel.updateTask(
                            task.copy(
                                taskText = editText,
                                taskDueDate = editDueDate,
                                filterTag = editTag
                            )
                        )
                        editingTask = null
                    }
                }
            )
        }
    }

    if (showCreateSheet) {
        var createText by remember { mutableStateOf("") }
        var createDueDate by remember { mutableStateOf<Long?>(null) }
        var createTag by remember { mutableStateOf<String?>(null) }
        var showDatePicker by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { showCreateSheet = false },
            sheetState = createSheetState
        ) {
            TaskEditSheetContent(
                title = stringResource(R.string.create_task_title),
                taskText = createText,
                onTaskTextChange = { createText = it },
                dueDate = createDueDate,
                onDueDateChange = { createDueDate = it },
                filterTag = createTag,
                onFilterTagChange = { createTag = it },
                filters = filterList,
                showDatePicker = showDatePicker,
                onShowDatePickerChange = { showDatePicker = it },
                onSave = {
                    if (createText.isNotBlank()) {
                        viewModel.addTask(
                            title = createText,
                            dueDate = createDueDate,
                            filterTag = createTag
                        )
                        createText = ""
                        createDueDate = null
                        createTag = null
                        showCreateSheet = false
                    }
                }
            )
        }
    }

    if (showFilterDialog) {
        CreateFilterDialog(
            onDismiss = { showFilterDialog = false },
            onConfirm = { name, color ->
                viewModel.addFilter(name, color)
                showFilterDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskEditSheetContent(
    title: String,
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    dueDate: Long?,
    onDueDateChange: (Long?) -> Unit,
    filterTag: String?,
    onFilterTagChange: (String?) -> Unit,
    filters: List<FilterTag>,
    showDatePicker: Boolean,
    onShowDatePickerChange: (Boolean) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            label = { Text(stringResource(R.string.task_content_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onShowDatePickerChange(true) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (dueDate != null) {
                    SimpleDateFormat("yyyy-MM-dd", LocalLocale.current.platformLocale).format(Date(dueDate))
                } else {
                    stringResource(R.string.set_due_date_hint)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (dueDate != null)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            if (dueDate != null) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onDueDateChange(null) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除日期",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TagSelector(
            selectedTag = filterTag,
            filters = filters,
            onTagSelected = { selected ->
                onFilterTagChange(if (selected == filterTag) null else selected)
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = taskText.isNotBlank()
        ) {
            Text(stringResource(R.string.save_button), style = MaterialTheme.typography.titleMedium)
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dueDate ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { onShowDatePickerChange(false) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = java.util.Calendar.getInstance().apply {
                            timeInMillis = millis
                            set(java.util.Calendar.HOUR_OF_DAY, 23)
                            set(java.util.Calendar.MINUTE, 59)
                            set(java.util.Calendar.SECOND, 59)
                        }
                        onDueDateChange(cal.timeInMillis)
                    }
                    onShowDatePickerChange(false)
                }) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { onShowDatePickerChange(false) }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun TagSelector(
    selectedTag: String?,
    filters: List<FilterTag>,
    onTagSelected: (String) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Label,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.select_tag_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (filters.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tags_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedTag == filter.name
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTagSelected(filter.name) },
                        label = { Text(filter.name) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(filter.color).copy(alpha = 0.2f),
                            selectedLabelColor = Color(filter.color)
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipsRow(
    filters: List<FilterTag>,
    selectedFilter: String?,
    onFilterClick: (String?) -> Unit,
    onAddFilterClick: () -> Unit,
    onDeleteFilter: (FilterTag) -> Unit
) {
    Surface(
        shadowElevation = 2.dp,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onFilterClick(null) },
                    label = {
                        Text(
                            stringResource(R.string.all_filter),
                            fontWeight = if (selectedFilter == null) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

            items(filters, key = { it.id }) { filter ->
                FilterChip(
                    selected = selectedFilter == filter.name,
                    onClick = { onFilterClick(filter.name) },
                    label = {
                        Text(
                            filter.name,
                            fontWeight = if (selectedFilter == filter.name)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    trailingIcon = if (selectedFilter == filter.name) {
                        {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "删除过滤器",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onDeleteFilter(filter) }
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(filter.color).copy(alpha = 0.2f),
                        selectedLabelColor = Color(filter.color)
                    )
                )
            }

            item {
                IconButton(
                    onClick = onAddFilterClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "添加过滤器",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateFilterDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, color: Long) -> Unit
) {
    var filterName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(filterPresetColors.first().first) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.create_filter_title), style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = filterName,
                    onValueChange = { filterName = it },
                    label = { Text(stringResource(R.string.filter_name_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.select_color_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filterPresetColors.forEach { (color, _) ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColor == color) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val name = filterName.trim()
                    if (name.isNotEmpty()) {
                        onConfirm(name, selectedColor)
                    }
                },
                enabled = filterName.isNotBlank(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.create_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ListCard(
    task: TodoTask,
    onCheckedChange: (Boolean) -> Unit,
    onRemove: (TodoTask) -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                scope.launch {
                    delay(200)
                    onRemove(task)
                }
            }
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
                        contentDescription = "删除",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                SwipeToDismissBoxValue.Settled -> {}
                else -> {}
            }
        }
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            onClick = onClick
        ) {
            val dueDate = task.taskDueDate
            val tag = task.filterTag
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckedChange
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = task.taskText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (dueDate != null || tag != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (dueDate != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = SimpleDateFormat(
                                            "MM-dd",
                                            LocalLocale.current.platformLocale
                                        ).format(Date(dueDate)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            if (tag != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}
