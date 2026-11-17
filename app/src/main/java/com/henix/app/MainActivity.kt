package com.henix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HenixTheme { HenixApp() } }
    }
}

enum class Tab(val label: String) { Home("Home"), Shorts("Shorts"), Me("Me") }

class AppState {
    val watchlist = mutableStateListOf("all-1", "all-2")
    val favorites = mutableStateListOf<String>()
    val downloads = mutableStateListOf<Pair<String, Int>>() // mediaId, episode
    var likedShorts = mutableStateListOf<String>()
    fun toggle(list: MutableList<String>, id: String) { if (!list.remove(id)) list.add(id) }
}

@Composable
fun HenixApp() {
    val state = remember { AppState() }
    var tab by rememberSaveable { mutableStateOf(Tab.Home) }
    var playing by remember { mutableStateOf<Media?>(null) }
    var searching by remember { mutableStateOf(false) }
    var showDownloads by remember { mutableStateOf(false) }

    BackHandler(enabled = playing != null || searching || showDownloads) {
        when { playing != null -> playing = null; searching -> searching = false; else -> showDownloads = false }
    }

    Box(Modifier.fillMaxSize().background(BrandDark)) {
        Scaffold(
            containerColor = BrandDark,
            bottomBar = {
                NavigationBar(containerColor = BrandNav) {
                    Tab.values().forEach { t ->
                        NavigationBarItem(
                            selected = tab == t, onClick = { tab = t },
                            icon = { Icon(when (t) { Tab.Home -> Icons.Default.Home; Tab.Shorts -> Icons.Default.PlayCircle; Tab.Me -> Icons.Default.Person }, t.label) },
                            label = { Text(t.label) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = BrandGreen, selectedTextColor = BrandGreen, indicatorColor = Color(0xFF1F2530))
                        )
                    }
                }
            }
        ) { pad ->
            Box(Modifier.padding(pad).fillMaxSize()) {
                when (tab) {
                    Tab.Home -> HomeScreen(state, onOpen = { playing = it }, onSearch = { searching = true })
                    Tab.Shorts -> ShortsScreen(state, onOpenMedia = { id -> Repo.media.find { it.id == id }?.let { playing = it } })
                    Tab.Me -> MeScreen(state, onOpen = { playing = it }, onDownloads = { showDownloads = true })
                }
            }
        }
        if (searching) SearchScreen(onClose = { searching = false }, onOpen = { searching = false; playing = it })
        if (showDownloads) DownloadsScreen(state, onClose = { showDownloads = false })
        playing?.let { PlayerScreen(it, state, onClose = { playing = null }) }
    }
}
