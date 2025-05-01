package com.uts.studentdo.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uts.studentdo.ui.TaskViewModel

// Define navigation routes
sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Long): String = "edit_task/$taskId"
    }
    object RecycleBin : Screen("recycle_bin")
    object Settings : Screen("settings")
}

@Composable
fun AppNavHost(
    taskViewModel: TaskViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.TaskList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                taskViewModel = taskViewModel,
                navigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                navigateToEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                },
                navigateToRecycleBin = { navController.navigate(Screen.RecycleBin.route) },
                navigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.AddTask.route) {
            AddTaskScreen(
                taskViewModel = taskViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            EditTaskScreen(
                taskId = taskId,
                taskViewModel = taskViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Screen.RecycleBin.route) {
            RecycleBinScreen(
                taskViewModel = taskViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                taskViewModel = taskViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}