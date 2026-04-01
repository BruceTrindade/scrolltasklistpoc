package com.example.cardslistpoccompose.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TaskDetailScreen(
    taskTitle: String,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = "Detalhe da etapa",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF757575),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = taskTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onCompleteClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Concluir tarefa")
        }
    }
}
