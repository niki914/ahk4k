package com.niki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun composeApp() {
    MaterialTheme {
        var showDialog by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf("") }

        // 示例数据列表
        val itemList = remember {
            (1..20).map { "列表项目 $it" }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题
            Text(
                text = "KMP Compose 示例界面",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // 按钮区域
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("显示对话框")
                }

                OutlinedButton(
                    onClick = { selectedItem = "按钮被点击了！" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("更新文本")
                }
            }

            // 文本显示区域
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "状态文本:",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (selectedItem.isNotEmpty()) selectedItem else "等待操作...",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // 滚动文本区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "滚动文本区域",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    repeat(10) { index ->
                        Text(
                            text = "这是第 ${index + 1} 行可滚动的文本内容。",
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            // 多项目列表
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "多项目列表",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(itemList) { item ->
                            ListItem(
                                headlineContent = { Text(item) },
                                supportingContent = { Text("点击选择此项目") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItem = item },
                                colors = ListItemDefaults.colors(
                                    containerColor = if (selectedItem == item)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }
            }
        }

        // 对话框
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("示例对话框") },
                text = {
                    Text("这是一个简单的对话框示例。\n当前选中的项目: ${selectedItem.ifEmpty { "无" }}")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            selectedItem = "对话框确认按钮被点击"
                        }
                    ) {
                        Text("确认")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}