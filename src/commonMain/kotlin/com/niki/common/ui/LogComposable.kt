package com.niki.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niki.common.logging.LogEntry
import com.niki.common.logging.LogLevel

@Composable
fun LogOutputCard(
    logs: List<LogEntry>, // <--- 接收 logs 作为参数
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "日志输出",
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            // 直接使用传入的 logs
            LogList(logs = logs)
        }
    }
}

// LogList 和 LogItem 保持不变，因为它们已经很好了
@Composable
fun LogList(logs: List<LogEntry>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(logs) { log ->
            LogItem(log = log)
        }
    }
}

@Composable
fun LogItem(log: LogEntry) {
    val (backgroundColor, textColor) = when (log.level) {
        LogLevel.VERBOSE -> Color.White to Color.Black
        LogLevel.DEBUG -> Color(0xFF6A8759) to Color.White
        LogLevel.INFO -> Color(0xFF315D77) to Color.White
        LogLevel.WARN -> Color(0xFFBBBB2A) to Color.Black
        LogLevel.ERROR -> Color(0xFFCF5B56) to Color.Black
    }

    Text(
        text = log.message,
        color = textColor,
        fontSize = 14.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .background(backgroundColor)
            .wrapContentSize(Alignment.CenterStart)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}