package com.onear.doplusplus.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onear.doplusplus.viewmodel.TodayViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { }, // 点击时打开对话框
                containerColor = MaterialTheme.colorScheme.primary, // 按钮背景色
                contentColor = MaterialTheme.colorScheme.onPrimary  // 图标颜色
            ) {
                // 使用 Material Design 的自带加号图标
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加任务"
                )
            }
        }
    ) {

    }

}