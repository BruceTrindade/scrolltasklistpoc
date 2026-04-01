package com.example.cardslistpoccompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cardslistpoccompose.TaskListViewModel
import com.example.cardslistpoccompose.ui.detail.TaskDetailScreen
import com.example.cardslistpoccompose.ui.list.TaskListScreen
import android.app.Activity

object AppDestinations {
    const val LIST = "list"
    const val DETAIL = "detail/{taskId}"
    fun detailRoute(taskId: String) = "detail/$taskId"
}

@Composable
fun AppNavHost(
    viewModel: TaskListViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LIST,
        modifier = modifier,
    ) {
        composable(AppDestinations.LIST) {
            TaskListScreen(
                tasks = tasks,
                onCloseClick = { (context as? Activity)?.finish() },
                onTaskClick = { task ->
                    if (!task.isCompleted) {
                        navController.navigate(AppDestinations.detailRoute(task.id))
                    }
                },
            )
        }
        composable(
            route = AppDestinations.DETAIL,
            arguments = listOf(
                navArgument("taskId") { type = NavType.StringType },
            ),
        ) { entry ->
            val taskId = entry.arguments?.getString("taskId").orEmpty()
            val task = tasks.firstOrNull { it.id == taskId }
            val title = task?.titleExpanded.orEmpty()
            TaskDetailScreen(
                taskTitle = title,
                onCompleteClick = {
                    if (taskId.isNotEmpty()) viewModel.completeTask(taskId)
                    navController.popBackStack()
                },
            )
        }
    }
}
