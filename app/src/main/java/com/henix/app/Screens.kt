package com.henix.app

import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Poster(m: Media, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.clickable(onClick = onClick)) {
        Box {
            AsyncImage(m.image, m.title, Modifier.fillMaxWidth().aspectRatio(0.7f).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
            Surface(Modifier.align(Alignment.TopStart).padding(6.dp), color = Color.Black.copy(.65f), shape = RoundedCornerShape(6.dp)) {
                Text(m.typeLabel, Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = BrandGreen, fontWeight = FontWeight.Bold)
            }
            Row(Modifier.align(Alignment.BottomEnd).padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Amber, modifier = Modifier.size(12.dp))
                Text(" ${m.rating}", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Text(m.title, Modifier.padding(top = 6.dp), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(m.year, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun SectionRow(title: String, items: List<Media>, onOpen: (Media) -> Unit) {
    Column {
        Text(title, Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { Poster(it, { onOpen(it) }, Modifier.width(120.dp)) }
        }
    }
}

@Composable
fun HomeScreen(state: AppState, onOpen: (Media) -> Unit, onSearch: () -> Unit) {
    var cat by remember { mutableStateOf("all") }
    val list = remember(cat) { if (cat == "all") Repo.media else Repo.media.filter { it.category == cat } }
    val featured = Repo.media.first()
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.background(BrandNav).padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                Modifier.fillMaxWidth().clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Purple.copy(.32f), Color(0xFF7E22CE).copy(.24f))))
                    .border(1.dp, Color(0xFFD8B4FE).copy(.35f), CircleShape)
                    .clickable(onClick = onSearch).padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Search here...", Modifier.weight(1f), color = Color(0xFFF3E8FF).copy(.8f), fontSize = 14.sp)
                Icon(Icons.Default.Search, null, tint = Color.White)
            }
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
                listOf("all" to "Home", "anime" to "Anime", "movie" to "Movie").forEach { (k, label) ->
                    Column(Modifier.padding(horizontal = 18.dp).clickable { cat = k }, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (cat == k) Color.White else Color.Gray)
                        Box(Modifier.padding(top = 4.dp).height(3.dp).width(34.dp).clip(CircleShape).background(if (cat == k) Amber else Color.Transparent))
                    }
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
            if (cat == "all") item {
                Box(Modifier.padding(16.dp).fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).clickable { onOpen(featured) }) {
                    AsyncImage(featured.image, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(.85f)))))
                    Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                        Text(featured.fullTitle, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, maxLines = 2)
                        Text(featured.description, fontSize = 12.sp, color = Color.LightGray, maxLines = 2)
                        Button(onClick = { onOpen(featured) }, Modifier.padding(top = 8.dp), colors = ButtonDefaults.buttonColors(BrandGreen, Color.Black)) {
                            Icon(Icons.Default.PlayArrow, null); Text("Watch now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item { SectionRow("Trending", list.sortedByDescending { it.rating }, onOpen) }
            item { SectionRow("Recently Added", list.reversed(), onOpen) }
            if (state.watchlist.isNotEmpty()) item { SectionRow("My Watchlist", Repo.media.filter { it.id in state.watchlist }, onOpen) }
        }
    }
}

@Composable
fun SearchScreen(onClose: () -> Unit, onOpen: (Media) -> Unit) {
    var q by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") }
    val res = Repo.media.filter { (filter == "all" || it.category == filter) && (q.isBlank() || it.fullTitle.contains(q, true)) }
    Column(Modifier.fillMaxSize().background(Color(0xFFFDF8EA)).statusBarsPadding()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClose) { Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF1F1E18)) }
            Row(Modifier.weight(1f).clip(CircleShape).background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(q, { q = it }, Modifier.weight(1f), singleLine = true, cursorBrush = SolidColor(Color.Black),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color(0xFF1F1E18), fontSize = 15.sp),
                    decorationBox = { if (q.isEmpty()) Text("Search movies, anime...", color = Color.Gray); it() })
                Icon(Icons.Default.Search, null, tint = Color.Gray)
            }
        }
        Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("all" to "All", "movie" to "Movie", "anime" to "Anime").forEach { (k, l) ->
                FilterChip(filter == k, { filter = k }, { Text(l) })
            }
        }
        LazyVerticalGrid(GridCells.Fixed(3), contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(res) { m -> CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides Color(0xFF1F1E18)) { Poster(m, { onOpen(m) }) } }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ShortsScreen(state: AppState, onOpenMedia: (String) -> Unit) {
    val pager = rememberPagerState { Repo.shorts.size }
    VerticalPager(pager, Modifier.fillMaxSize().background(Color.Black)) { i ->
        val s = Repo.shorts[i]
        val liked = s.id in state.likedShorts
        Box(Modifier.fillMaxSize().pointerInputDoubleTap { if (!liked) state.likedShorts.add(s.id) }) {
            AsyncImage(s.image, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(.8f)))))
            Column(Modifier.align(Alignment.CenterEnd).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                IconButton({ state.toggle(state.likedShorts, s.id) }) { Icon(Icons.Default.Favorite, null, tint = if (liked) BrandPink else Color.White, modifier = Modifier.size(34.dp)) }
                Text(s.likes, fontSize = 12.sp)
                Icon(Icons.Default.ChatBubble, null, tint = Color.White); Text(s.comments, fontSize = 12.sp)
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp).padding(end = 70.dp)) {
                Text(s.creator, fontWeight = FontWeight.Bold)
                Text(s.caption, fontSize = 13.sp)
                Text("♪ ${s.audio}", fontSize = 11.sp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
                AssistChip({ onOpenMedia(s.linkedMediaId) }, { Text(s.pill) }, Modifier.padding(top = 8.dp),
                    leadingIcon = { Icon(Icons.Default.PlayArrow, null) })
            }
        }
    }
}

fun Modifier.pointerInputDoubleTap(onDouble: () -> Unit): Modifier =
    this.then(Modifier.pointerInput(Unit) { detectTapGestures(onDoubleTap = { onDouble() }) })

@Composable
fun MeScreen(state: AppState, onOpen: (Media) -> Unit, onDownloads: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(64.dp).clip(CircleShape).background(Color.Black).border(1.dp, Color.White.copy(.25f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = BrandPink, modifier = Modifier.size(36.dp))
                }
                Column(Modifier.padding(start = 14.dp)) { Text("Your space", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold); Text("Guest • Tap to sign in", color = Color.Gray, fontSize = 12.sp) }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(BrandCard), modifier = Modifier.fillMaxWidth().clickable(onClick = onDownloads)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Download, null, tint = BrandGreen); Text("  Downloads (${state.downloads.size})", fontWeight = FontWeight.Bold)
                }
            }
        }
        item { SectionRow("Watchlist", Repo.media.filter { it.id in state.watchlist }, onOpen) }
        item { SectionRow("Favorites", Repo.media.filter { it.id in state.favorites }, onOpen) }
    }
}

@Composable
fun DownloadsScreen(state: AppState, onClose: () -> Unit) {
    Column(Modifier.fillMaxSize().background(BrandDark).statusBarsPadding()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            IconButton(onClose) { Icon(Icons.Default.ArrowBack, null) }
            Text("Downloads", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
        if (state.downloads.isEmpty()) Box(Modifier.fillMaxSize(), Alignment.Center) { Text("No downloads yet", color = Color.Gray) }
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.downloads.toList()) { (id, ep) ->
                val m = Repo.media.first { it.id == id }
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(BrandCard).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(m.image, null, Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) { Text(m.fullTitle, maxLines = 1, fontWeight = FontWeight.Bold); Text("Episode $ep • ${Repo.episodes[ep - 1].sizeMB} MB", fontSize = 12.sp, color = Color.Gray) }
                    IconButton({ state.downloads.remove(id to ep) }) { Icon(Icons.Default.Delete, null, tint = BrandPink) }
                }
            }
        }
    }
}

@Composable
fun PlayerScreen(m: Media, state: AppState, onClose: () -> Unit) {
    var playing by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0.12f) }
    var ep by remember { mutableIntStateOf(1) }
    LaunchedEffect(playing, ep) { while (playing && progress < 1f) { kotlinx.coroutines.delay(500); progress = (progress + 0.004f).coerceAtMost(1f) } }
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0D11)).statusBarsPadding()) {
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Color.Black)) {
            AsyncImage(m.image, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            IconButton(onClose, Modifier.align(Alignment.TopStart)) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
            IconButton({ playing = !playing }, Modifier.align(Alignment.Center).size(56.dp).background(Color.Black.copy(.5f), CircleShape)) {
                Icon(if (playing) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(34.dp))
            }
            Slider(progress, { progress = it }, Modifier.align(Alignment.BottomCenter).padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(thumbColor = BrandGreen, activeTrackColor = BrandGreen))
        }
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text(m.fullTitle, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text("${m.typeLabel} • ${m.year} • ★ ${m.rating}", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                Text(m.description, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp), color = Color.LightGray)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val fav = m.id in state.favorites; val wl = m.id in state.watchlist
                    OutlinedButton({ state.toggle(state.favorites, m.id) }) { Icon(Icons.Default.Favorite, null, tint = if (fav) BrandPink else Color.White); Text(" Favorite") }
                    OutlinedButton({ state.toggle(state.watchlist, m.id) }) { Icon(Icons.Default.Bookmark, null, tint = if (wl) BrandGreen else Color.White); Text(" Watchlist") }
                }
            }
            item { Text("Episodes", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            items(Repo.episodes) { e ->
                val done = (m.id to e.num) in state.downloads
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (ep == e.num) Color(0xFF1F2530) else BrandCard)
                    .clickable { ep = e.num; progress = 0f; playing = true }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("EP ${e.num}", fontWeight = FontWeight.Bold, color = if (ep == e.num) BrandGreen else Color.White)
                    Text("  ${e.duration} • ${e.sizeMB} MB", Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray)
                    IconButton({ val p = m.id to e.num; if (done) state.downloads.remove(p) else state.downloads.add(p) }) {
                        Icon(if (done) Icons.Default.CheckCircle else Icons.Default.Download, null, tint = if (done) BrandGreen else Color.White)
                    }
                }
            }
        }
    }
}
