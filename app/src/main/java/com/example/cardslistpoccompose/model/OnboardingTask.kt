package com.example.cardslistpoccompose.model

data class OnboardingTask(
    val id: String,
    val titleExpanded: String,
    val subtitleExpanded: String,
    val categoryLabel: String,
    val completedStatusText: String,
    val isCompleted: Boolean,
)
