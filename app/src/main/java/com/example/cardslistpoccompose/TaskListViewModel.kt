package com.example.cardslistpoccompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardslistpoccompose.model.OnboardingTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskListViewModel : ViewModel() {

    private val _tasks = MutableStateFlow(mockTasks().sortedForDisplay())
    val tasks: StateFlow<List<OnboardingTask>> = _tasks.asStateFlow()

    /**
     * Task ID that should be completed when list screen becomes visible.
     * Set by detail screen, consumed by list screen after running completion animation.
     */
    private val _pendingTaskToComplete = MutableStateFlow<String?>(null)
    val pendingTaskToComplete: StateFlow<String?> = _pendingTaskToComplete.asStateFlow()

    /**
     * When non-null, list screen should scroll to first pending after reorder animation.
     * Cleared by [consumePendingScrollToFirstTodo] after handling.
     */
    private val _pendingScrollAfterComplete = MutableStateFlow<String?>(null)
    val pendingScrollAfterComplete: StateFlow<String?> = _pendingScrollAfterComplete.asStateFlow()

    fun consumePendingScrollToFirstTodo() {
        _pendingScrollAfterComplete.value = null
    }

    /** Called by detail screen to schedule completion (not execute immediately). */
    fun scheduleTaskCompletion(id: String) {
        _pendingTaskToComplete.value = id
    }

    /** Called by list screen to consume pending completion and run the actual state change. */
    fun executePendingCompletion(): String? {
        val id = _pendingTaskToComplete.value ?: return null
        _pendingTaskToComplete.value = null
        completeTask(id)
        return id
    }

    fun completeTask(id: String) {
        _tasks.update { list ->
            val taskToComplete = list.firstOrNull { it.id == id } ?: return@update list
            if (taskToComplete.isCompleted) return@update list

            val nextRank =
                list.filter { it.isCompleted }.mapNotNull { it.completedRank }.maxOrNull()?.plus(1)
                    ?: 0

            list
                .map { task ->
                    if (task.id == id) {
                        task.copy(isCompleted = true, completedRank = nextRank)
                    } else {
                        task
                    }
                }
                .sortedForDisplay()
        }
        _pendingScrollAfterComplete.value = id
    }
}

private fun List<OnboardingTask>.sortedForDisplay(): List<OnboardingTask> =
    sortedWith(
        compareBy<OnboardingTask>({ !it.isCompleted })
            .thenBy { it.completedRank ?: Int.MAX_VALUE }
            .thenBy { it.id.toIntOrNull() ?: Int.MAX_VALUE },
    )

private fun mockTasks(): List<OnboardingTask> = listOf(
    OnboardingTask(
        id = "1",
        titleExpanded = "Compras online no crédito? Pague sempre com um cartão virtual",
        subtitleExpanded = "Crie seu cartão virtual e comece a usar.",
        categoryLabel = "Cartões",
        completedStatusText = "Cartão virtual criado",
        isCompleted = true,
        completedRank = 0,
    ),
    OnboardingTask(
        id = "2",
        titleExpanded = "Envie e receba dinheiro sem complicação",
        subtitleExpanded = "Cadastre sua chave Pix.",
        categoryLabel = "Pix",
        completedStatusText = "Pix ativado",
        isCompleted = true,
        completedRank = 1,
    ),
    OnboardingTask(
        id = "3",
        titleExpanded = "Dinheiro na conta",
        subtitleExpanded = "Receba transferências e utilize com tranquilidade.",
        categoryLabel = "Dinheiro na conta",
        completedStatusText = "Recebido com sucesso",
        isCompleted = false,
        completedRank = 2,
    ),
    OnboardingTask(
        id = "4",
        titleExpanded = "Comece já a fazer compras online ou por aproximação",
        subtitleExpanded = "Ative seu cartão.",
        categoryLabel = "Compras",
        completedStatusText = "Cartão ativado",
        isCompleted = false,
    ),
    OnboardingTask(
        id = "5",
        titleExpanded = "Personalize alertas e limites do seu jeito",
        subtitleExpanded = "Acesse as configurações de segurança.",
        categoryLabel = "Segurança",
        completedStatusText = "Preferências salvas",
        isCompleted = false,
        pendingStatusLabel = "Solicitação pendente",
    ),
)
