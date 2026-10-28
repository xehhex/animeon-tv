package com.animeon.tv.ui.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.animeon.tv.data.model.EpisodeInfo
import com.animeon.tv.ui.theme.BgDark
import com.animeon.tv.ui.theme.FocusGold
import com.animeon.tv.ui.theme.RedAccent
import com.animeon.tv.ui.theme.SurfaceDarkVariant
import com.animeon.tv.ui.theme.TextMuted
import com.animeon.tv.ui.theme.TextWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DetailScreen(
    animeUrl: String,
    onEpisodeSelected: (String, String, String) -> Unit, // videoLink, animeTitle, epNumber
    viewModel: DetailViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(animeUrl) {
        viewModel.loadAnime(animeUrl)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Загрузка тайтла...", color = TextMuted, fontSize = 20.sp)
            }
        } else {
            val anime = state.anime
            if (anime != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 48.dp)
                ) {
                    // Top header: poster + info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 48.dp, end = 48.dp, top = 40.dp)
                    ) {
                        AsyncImage(
                            model = anime.displayPosterUrl,
                            contentDescription = anime.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(200.dp)
                                .aspectRatio(0.7f)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.width(32.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = anime.title,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            anime.titleOrig?.let { orig ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = orig,
                                    fontSize = 16.sp,
                                    color = TextMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Год: ${anime.year ?: "-"} | Тип: ${anime.kind ?: "-"} | Статус: ${anime.status ?: "-"}",
                                color = TextMuted,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = anime.description?.takeIf { it.isNotBlank() } ?: "Описание пока отсутствует.",
                                color = TextWhite.copy(alpha = 0.85f),
                                fontSize = 14.sp,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Voice / Translations Selection
                    if (state.availableTranslations.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            text = "Озвучка / Перевод:",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 48.dp, bottom = 12.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 48.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.availableTranslations) { trans ->
                                val isSelected = trans == state.selectedTranslation
                                Button(
                                    onClick = { viewModel.selectTranslation(trans) },
                                    colors = ButtonDefaults.colors(
                                        containerColor = if (isSelected) RedAccent else SurfaceDarkVariant,
                                        contentColor = TextWhite
                                    ),
                                    shape = ButtonDefaults.shape(RoundedCornerShape(8.dp))
                                ) {
                                    Text(text = trans, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }

                    // Episodes Row
                    if (state.episodes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            text = "Серии (${state.episodes.size}):",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 48.dp, bottom = 12.dp)
                        )

                        val sortedEpisodes = state.episodes.entries.sortedBy { it.key.toIntOrNull() ?: 0 }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 48.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(sortedEpisodes) { (epNum, epInfo) ->
                                EpisodeCard(
                                    episodeNumber = epNum,
                                    episodeInfo = epInfo,
                                    onClick = {
                                        epInfo.link?.let { link ->
                                            onEpisodeSelected(link, anime.title, epNum)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EpisodeCard(
    episodeNumber: String,
    episodeInfo: EpisodeInfo,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isFocused) 1.08f else 1.0f, label = "ep_scale")

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(180.dp)
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused },
        shape = CardDefaults.shape(RoundedCornerShape(8.dp)),
        border = CardDefaults.border(
            focusedBorder = Border(border = BorderStroke(2.5.dp, FocusGold))
        ),
        scale = CardDefaults.scale(focusedScale = 1.0f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDarkVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(BgDark)
            ) {
                val screenshot = episodeInfo.screenshots?.firstOrNull()
                if (screenshot != null) {
                    AsyncImage(
                        model = screenshot,
                        contentDescription = "Серия $episodeNumber",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .background(BgDark.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Серия $episodeNumber",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
