package com.onear.doplusplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onear.doplusplus.ui.screen.auth.LoginScreen
import com.onear.doplusplus.ui.screen.main.MainScreen
import com.onear.doplusplus.ui.screen.other.AboutScreen
import com.onear.doplusplus.ui.screen.other.SettingScreen
import com.onear.doplusplus.ui.theme.DoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.MAIN
                ) {
                    composable(
                        NavRoutes.MAIN,
                        // main 页面的退出动画（被覆盖时向左滑出）
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth / 3 }
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        // 回到 main 时的入场动画（从左边滑回来）
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth / 3 }
                            ) + fadeIn(animationSpec = tween(300))
                        }) {
                        MainScreen(
                            onNavigate = { route ->
                                navController.navigate(route)
                            }
                        )
                    }
                    composable(
                        NavRoutes.LOGIN,
                        // login 页面的入场动画（从右边滑入）
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        // login 页面的退出动画（回到上一页时向右滑出）
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }
                            ) + fadeOut(animationSpec = tween(300))
                        }) {
                        LoginScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        NavRoutes.SETTINGS,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        SettingScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        NavRoutes.ABOUT,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        AboutScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
) {
    TODAY(R.string.nav_today, Icons.Default.DateRange),
    TODO(R.string.nav_todo, Icons.Default.List),
    PROFILE(R.string.nav_profile, Icons.Default.Person),
}

@Composable
fun Greeting(content: String, modifier: Modifier = Modifier) {
    MaterialTheme() {
        Text(
            text = content,
            fontSize = 30.sp,
            modifier = modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoTheme {
        Greeting("Android")
    }
}

object NavRoutes {
    const val MAIN = "main"
    const val LOGIN = "login"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
}

data class Message(val author: String, val content: String)
