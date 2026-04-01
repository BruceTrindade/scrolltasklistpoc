package com.example.cardslistpoccompose

import androidx.lifecycle.ViewModel
import com.example.cardslistpoccompose.model.OnboardingTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskListViewModel : ViewModel() {

    private val _tasks = MutableStateFlow(mockTasks())
    val tasks: StateFlow<List<OnboardingTask>> = _tasks.asStateFlow()

    fun completeTask(id: String) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == id) task.copy(isCompleted = true) else task
            }
        }
    }
}

private fun mockTasks(): List<OnboardingTask> = listOf(
    OnboardingTask(
        id = "1",
        titleExpanded = "Compras online no crédito? Pague sempre com um cartão virtual",
        subtitleExpanded = "Crie seu cartão virtual e comece a usar.",
        categoryLabel = "Cartões",
        completedStatusText = "Cartão virtual criado",
        isCompleted = true,
    ),
    OnboardingTask(
        id = "2",
        titleExpanded = "Envie e receba dinheiro sem complicação",
        subtitleExpanded = "Cadastre sua chave Pix.",
        categoryLabel = "Pix",
        completedStatusText = "Pix ativado",
        isCompleted = true,
    ),
    OnboardingTask(
        id = "3",
        titleExpanded = "Dinheiro na conta",
        subtitleExpanded = "Receba transferências e utilize com tranquilidade.",
        categoryLabel = "Dinheiro na conta",
        completedStatusText = "Recebido com sucesso",
        isCompleted = true,
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
    ),
)
