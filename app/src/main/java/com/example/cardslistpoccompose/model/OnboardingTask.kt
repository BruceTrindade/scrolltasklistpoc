package com.example.cardslistpoccompose.model

data class OnboardingTask(
    val id: String,
    val titleExpanded: String,
    val subtitleExpanded: String,
    val categoryLabel: String,
    val completedStatusText: String,
    val isCompleted: Boolean,
    /** Optional badge for pending tasks (e.g. "Solicitação pendente"); shown with clock icon when non-null. */
    val pendingStatusLabel: String? = null,
    /**
     * Order among completed tasks (lower = closer to top). Null when [isCompleted] is false.
     * Assigned when the task is marked done so completed tasks always sort above pending ones.
     */
    val completedRank: Int? = null,
)
