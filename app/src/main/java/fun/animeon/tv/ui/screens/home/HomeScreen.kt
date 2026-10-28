package fun.animeon.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import fun.animeon.tv.data.model.AnimeItem
import fun.animeon.tv.ui.components.HeroBanner
import fun.animeon.tv.ui.components.SwimlaneRow
import androidx.compose.ui.text.font.FontWeight
import fun.animeon.tv.ui.theme.BgDark
import fun.animeon.tv.ui.theme.RedAccent
import fun.animeon.tv.ui.theme.SurfaceDarkVariant
import fun.animeon.tv.ui.theme.TextMuted
import fun.animeon.tv.ui.theme.TextWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    onAnimeSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Загрузка AnimeOn TV...", color = TextMuted, fontSize = 20.sp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Bar with Search
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AnimeOn",
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = " TV",
                        color = RedAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onSearchClick,
                        colors = ButtonDefaults.colors(
                            containerColor = SurfaceDarkVariant,
                            contentColor = TextWhite
                        ),
                        shape = ButtonDefaults.shape(RoundedCornerShape(8.dp))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Поиск")
                        }
                    }
                }

                // Hero Banner
                state.heroItem?.let { hero ->
                    HeroBanner(
                        title = hero.title,
                        genres = hero.displayGenres,
                        backdropUrl = hero.displayPosterUrl,
                        onWatchClick = { onAnimeSelected(hero.animeUrl) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Schedule as AnimeItems
                val scheduleAnimeItems = state.scheduleList.map {
                    AnimeItem(
                        id = it.id,
                        title = it.title,
                        titleOrig = it.titleOrig,
                        animeUrl = it.animeUrl,
                        poster = it.posterUrl,
                        rating = it.rating
                    )
                }

                SwimlaneRow(
                    title = "Расписание новых серий",
                    items = scheduleAnimeItems,
                    onItemClick = { onAnimeSelected(it.animeUrl) }
                )

                SwimlaneRow(
                    title = "Популярные аниме",
                    items = state.popularList,
                    onItemClick = { onAnimeSelected(it.animeUrl) }
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
