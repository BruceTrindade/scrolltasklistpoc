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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
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

private val CardShape = RoundedCornerShape(22.dp)
private val CardInnerPadding = 22.dp
private val CompletedGreen = Color(0xFF2E7D32)
private val PendingStatusBlue = Color(0xFF1565C0)

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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 6.dp,
            focusedElevation = 6.dp,
            hoveredElevation = 8.dp,
        ),
    ) {
        if (task.isCompleted) {
            TaskCardCompletedContent(task = task)
        } else {
            TaskCardPendingContent(task = task)
        }
    }
}

@Composable
private fun TaskCardCompletedContent(task: OnboardingTask) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CardInnerPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.categoryLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF757575),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.completedStatusText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Concluída",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                )
            }

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
        }

        Spacer(modifier = Modifier.height(12.dp))

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
                tint = Color(0xFFBDBDBD),
            )
        }
    }
}

@Composable
private fun TaskCardPendingContent(task: OnboardingTask) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CardInnerPadding),
    ) {
        Text(
            text = task.categoryLabel,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF757575),
        )
        Spacer(modifier = Modifier.height(4.dp))

        val status = task.pendingStatusLabel
        if (status != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = PendingStatusBlue,
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelLarge,
                    color = PendingStatusBlue,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = task.titleExpanded,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = task.subtitleExpanded,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF757575),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp),
                tint = Color(0xFF9E9E9E),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = Color(0xFF424242),
            )
        }
    }
}
