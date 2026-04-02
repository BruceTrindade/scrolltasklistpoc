package com.example.cardslistpoccompose.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cardslistpoccompose.TaskListViewModel
import com.example.cardslistpoccompose.model.OnboardingTask
import kotlinx.coroutines.delay

private val ScreenBackground = Color(0xFFECECEC)
private val ScreenHorizontalPadding = 16.dp

@Composable
fun TaskListScreen(
    tasks: List<OnboardingTask>,
    viewModel: TaskListViewModel,
    onCloseClick: () -> Unit,
    onTaskClick: (OnboardingTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val pendingTaskToComplete by viewModel.pendingTaskToComplete.collectAsStateWithLifecycle()
    val pendingScroll by viewModel.pendingScrollAfterComplete.collectAsStateWithLifecycle()

    // Step 1: Execute pending completion after screen appears (user sees the transition)
    LaunchedEffect(pendingTaskToComplete) {
        if (pendingTaskToComplete == null) return@LaunchedEffect
        delay(250) // Let the list render first
        viewModel.executePendingCompletion()
    }

    // Step 2: After completion triggers reorder animation, scroll to next pending task
    LaunchedEffect(pendingScroll, tasks) {
        if (pendingScroll == null) return@LaunchedEffect
        delay(400) // Wait for reorder animation to play
        val pendingIndex = tasks.indexOfFirst { task -> !task.isCompleted }
        if (pendingIndex >= 0) {
            listState.animateScrollToItem(pendingIndex)
        }
        viewModel.consumePendingScrollToFirstTodo()
    }

    val completed = tasks.count { it.isCompleted }
    val total = tasks.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = ScreenHorizontalPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
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
                    .padding(end = 48.dp),
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
            listState = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onTaskClick = onTaskClick,
        )
    }
}
