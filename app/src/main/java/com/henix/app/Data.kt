package com.henix.app

data class Media(
    val id: String, val title: String, val fullTitle: String, val category: String,
    val typeLabel: String, val rating: Double, val year: String, val image: String, val description: String
)

data class Short(
    val id: String, val creator: String, val image: String, val caption: String,
    val audio: String, val likes: String, val comments: String, val linkedMediaId: String, val pill: String
)

data class Episode(val num: Int, val sizeMB: Double, val duration: String)

private fun u(id: String, w: Int = 600) = "https://images.unsplash.com/$id?w=$w&auto=format&fit=crop&q=80"

object Repo {
    val media = listOf(
        Media("featured-1", "That Time I Got Reincarnated as a...", "That Time I Got Reincarnated as a Slime", "anime", "Anime", 8.8, "2024", u("photo-1578632767115-351597cf2477"), "Season 3 Now Streaming in HD. Rimuru Tempest expands his nation while facing powerful new adversaries."),
        Media("all-1", "The Red Ranger...", "The Red Ranger Becomes an Adventurer in Another World", "anime", "Anime", 8.8, "2024", u("photo-1578632767115-351597cf2477"), "An action-packed fantasy adventure following a heroic warrior transported to another realm."),
        Media("all-2", "Taisho Otome F...", "Taisho Otome Fairy Tale", "anime", "Anime", 8.8, "2024", u("photo-1607604276583-eef5d076aa5f"), "A heartwarming Taisho-era tale of resilience, companionship, and hope."),
        Media("all-3", "Blue Box [Hindi]", "Blue Box [Hindi]", "anime", "Anime", 8.0, "2024", u("photo-1451187580459-43490279c0fa"), "Taiki Inomata, a badminton player, admires basketball star Chinatsu Kano as they pursue their sports dreams together."),
        Media("1", "The Great Indian Kapil Show", "The Great Indian Kapil Show", "movie", "Show", 8.5, "2024", u("photo-1514525253161-7a46d19cd819"), "Kapil Sharma brings celebrity guests, comedy sketches, and entertainment."),
        Media("3", "Interstellar", "Interstellar", "movie", "Movie", 8.7, "2014", u("photo-1451187580459-43490279c0fa"), "A team of explorers travel through a wormhole in space."),
        Media("5", "Inception", "Inception", "movie", "Movie", 8.8, "2010", u("photo-1536440136628-849c177e76a1"), "A thief who enters dreams receives a chance to regain his old life."),
        Media("6", "Jujutsu Kaisen", "Jujutsu Kaisen", "anime", "Anime", 8.6, "2023", u("photo-1618005182384-a83a8bd57fbe"), "Yuji Itadori joins Jujutsu Sorcerers to kill powerful curses.")
    )

    val shorts = listOf(
        Short("short-1", "@henix_official", u("photo-1578632767115-351597cf2477", 800), "Epic Anime Fight Scenes in 60FPS! 🔥 #anime #trending", "Original Anime OST", "142K", "1.2K", "featured-1", "Watch Full Anime"),
        Short("short-2", "@the_kapil_show", u("photo-1514525253161-7a46d19cd819", 800), "Comedy night highlights 😂 #kapil #show", "Original Audio", "98K", "860", "1", "Watch Full Show"),
        Short("short-3", "@henix_official", u("photo-1618005182384-a83a8bd57fbe", 800), "Domain expansion edit ⚡ #jjk #anime", "Anime Remix", "76K", "540", "6", "Watch Full Anime")
    )

    val episodes = (1..13).map { Episode(it, listOf(207.7,192.3,198.4,162.9,238.1,224.5,215.0,230.4,219.8,241.2,228.6,235.0,243.4)[it-1], "25:3${it % 10}") }
}
