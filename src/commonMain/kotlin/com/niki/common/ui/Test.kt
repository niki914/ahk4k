//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//import androidx.compose.ui.window.rememberWindowState
//
//fun dialog() = application(false) {
//    val windowState = rememberWindowState(width = 300.dp, height = 200.dp)
//
//    Window(
//        onCloseRequest = ::exitApplication,
//        undecorated = true, // 移除系统边框
//        transparent = true, // 透明背景
//        state = windowState
//    ) {
//        MaterialTheme {
//            // MD3 风格的对话框容器
//            Surface(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f)) // Fully transparent background for the window area
//                    .wrapContentSize(Alignment.Center),
//                color = MaterialTheme.colorScheme.surface, // Dialog surface color
//                shape = RoundedCornerShape(16.dp), // MD3 large shape for dialog
////                shadowElevation = 6.dp // MD3 recommends elevation levels (e.g., 6.dp for dialogs)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .width(280.dp) // Maintain a fixed width for the content
//                        .padding(24.dp), // Increased padding for MD3 feel
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "MD3 风格对话框",
//                        style = MaterialTheme.typography.headlineSmall, // Larger title for MD3
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "这是一个符合 Material 3 设计规范的对话框，并且支持拖动。",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant, // Use onSurfaceVariant for body text
//                        modifier = Modifier.padding(horizontal = 8.dp) // Slight horizontal padding for text
//                    )
//                    Spacer(modifier = Modifier.height(24.dp)) // Increased spacing before actions
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End // Align actions to the end
//                    ) {
//                        TextButton(
//                            onClick = ::exitApplication,
//                            colors = ButtonDefaults.textButtonColors(
//                                contentColor = MaterialTheme.colorScheme.primary // MD3 text button color
//                            )
//                        ) {
//                            Text("关闭")
//                        }
//                        Spacer(modifier = Modifier.width(8.dp)) // Spacing between buttons
//                        Button(
//                            onClick = ::exitApplication,
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = MaterialTheme.colorScheme.primary,
//                                contentColor = MaterialTheme.colorScheme.onPrimary
//                            ),
//                            shape = RoundedCornerShape(20.dp) // MD3 button shape for filled button (capsule-like)
//                        ) {
//                            Text("确认")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}