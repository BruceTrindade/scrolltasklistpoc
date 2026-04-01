package com.example.cardslistpoccompose.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardslistpoccompose.model.OnboardingTask

private val ScreenBackground = Color(0xFFECECEC)

@Composable
fun TaskListScreen(
    tasks: List<OnboardingTask>,
    onCloseClick: () -> Unit,
    onTaskClick: (OnboardingTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    val completed = tasks.count { it.isCompleted }
    val total = tasks.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 4.dp, top = 12.dp, bottom = 8.dp),
        ) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Fechar",
                    tint = Color(0xFF424242),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp),
            ) {
                Text(
                    text = "Deixe sua conta pronta para facilitar seu dia a dia",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    lineHeight = 28.sp,
                )
                Text(
                    text = "$completed de $total etapas concluídas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                )
            }
        }
        StackedTaskList(
            tasks = tasks,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            onTaskClick = onTaskClick,
        )
    }
}
