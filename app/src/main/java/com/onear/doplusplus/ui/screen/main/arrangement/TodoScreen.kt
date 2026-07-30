package com.onear.doplusplus.ui.screen.main.arrangement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onear.doplusplus.R
import com.onear.doplusplus.data.entity.FilterTag
import com.onear.doplusplus.data.entity.TodoTask
import com.onear.doplusplus.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale
import com.onear.doplusplus.ui.screen.todo.*
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel
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
                EmptyTaskPlaceHolder(modifier.weight(1f))
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
            if (inputText != "") {
                createText = inputText
            }
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



@Composable
private fun EmptyTaskPlaceHolder(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_tasks),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
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
                    SimpleDateFormat("yyyy-MM-dd", LocalLocale.current.platformLocale).format(
                        Date(
                            dueDate
                        )
                    )
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
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = millis
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
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




