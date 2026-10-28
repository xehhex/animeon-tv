package fun.animeon.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fun.animeon.tv.ui.screens.detail.DetailScreen
import fun.animeon.tv.ui.screens.home.HomeScreen
import fun.animeon.tv.ui.screens.player.PlayerScreen
import fun.animeon.tv.ui.screens.search.SearchScreen
import fun.animeon.tv.ui.theme.AnimeOnTvTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeOnTvTheme {
                AnimeOnTvApp()
            }
        }
    }
}

@Composable
fun AnimeOnTvApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            HomeScreen(
                onAnimeSelected = { animeUrl ->
                    navController.navigate("detail/$animeUrl")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }

        composable(
            route = "detail/{animeUrl}",
            arguments = listOf(navArgument("animeUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val animeUrl = backStackEntry.arguments?.getString("animeUrl") ?: ""
            DetailScreen(
                animeUrl = animeUrl,
                onEpisodeSelected = { videoLink, animeTitle, epNumber ->
                    val encodedLink = URLEncoder.encode(videoLink, StandardCharsets.UTF_8.toString())
                    val encodedTitle = URLEncoder.encode(animeTitle, StandardCharsets.UTF_8.toString())
                    navController.navigate("player/$encodedLink/$encodedTitle/$epNumber")
                }
            )
        }

        composable(
            route = "player/{videoLink}/{animeTitle}/{episodeNumber}",
            arguments = listOf(
                navArgument("videoLink") { type = NavType.StringType },
                navArgument("animeTitle") { type = NavType.StringType },
                navArgument("episodeNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rawLink = backStackEntry.arguments?.getString("videoLink") ?: ""
            val rawTitle = backStackEntry.arguments?.getString("animeTitle") ?: ""
            val epNum = backStackEntry.arguments?.getString("episodeNumber") ?: "1"

            val videoLink = URLDecoder.decode(rawLink, StandardCharsets.UTF_8.toString())
            val title = URLDecoder.decode(rawTitle, StandardCharsets.UTF_8.toString())

            PlayerScreen(
                videoLink = videoLink,
                animeTitle = title,
                episodeNumber = epNum,
                onBack = { navController.popBackStack() }
            )
        }

        composable("search") {
            SearchScreen(
                onAnimeSelected = { animeUrl ->
                    navController.navigate("detail/$animeUrl")
                }
            )
        }
    }
}
