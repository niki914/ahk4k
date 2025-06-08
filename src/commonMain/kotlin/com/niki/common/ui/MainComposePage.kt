package com.niki.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
            Button(onClick = { MainViewModel.isShowingDialog.value = true }) {
                Text(text = "记录热键(仅记录无实际作用)")
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