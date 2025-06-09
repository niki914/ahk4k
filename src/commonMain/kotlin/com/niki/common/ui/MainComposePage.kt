package com.niki.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niki.common.logging.logE
import com.niki.common.mvvm.MainViewModel
import com.niki.windows.toCommaSeparatedString

@Composable
fun MainComposePage() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HotStringInputCard()
            Row(
                modifier = Modifier.fillMaxWidth(), // Make the Row fill the width
                horizontalArrangement = Arrangement.SpaceAround // Distribute space evenly
            ) {
                Button(
                    onClick = { MainViewModel.isShowingDialog.value = true },
                    modifier = Modifier.weight(1f) // Give the button equal weight
                ) {
                    Text(text = "记录热键(仅记录无实际作用)")
                }
                Button(
                    onClick = { MainViewModel.installGenshin() },
                    modifier = Modifier.weight(1f) // Give the new button equal weight
                ) {
                    Text(text = "下载原神") // Text for the new button
                }
            }
            LogOutputCard(modifier = Modifier.weight(1f))

            if (MainViewModel.isShowingDialog.collectAsState().value) {
                CountdownDialog(
                    countSecond = 4,
                    onDismissRequest = { MainViewModel.isShowingDialog.value = false },
                    onConfirm = { set ->
                        val str = set.toCommaSeparatedString { this::class.java.simpleName }
                        logE("记录热键: $str")
                    },
                    dataFlow = MainViewModel.currentKeys
                )
            }
        }
    }
}