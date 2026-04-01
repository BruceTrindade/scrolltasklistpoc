package com.example.cardslistpoccompose.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardslistpoccompose.model.OnboardingTask

private val CardShape = RoundedCornerShape(16.dp)
private val CompletedGreen = Color(0xFF2E7D32)

@Composable
fun TaskCard(
    task: OnboardingTask,
    height: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .then(
                if (!task.isCompleted) Modifier.clickable(onClick = onClick)
                else Modifier,
            ),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        TaskCardFixedLayout(task = task)
    }
}

@Composable
private fun TaskCardFixedLayout(task: OnboardingTask) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val topLabel = if (task.isCompleted) task.categoryLabel else null
                if (topLabel != null) {
                    Text(
                        text = topLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF757575),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = if (task.isCompleted) task.completedStatusText else task.titleExpanded,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (task.isCompleted) "Concluída" else task.subtitleExpanded,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                )
            }

            if (task.isCompleted) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CompletedGreen),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Área visual inferior (placeholder), mantém layout fixo como no XML
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = if (task.isCompleted) Color(0xFFBDBDBD) else Color(0xFF424242),
            )
        }
    }
}
