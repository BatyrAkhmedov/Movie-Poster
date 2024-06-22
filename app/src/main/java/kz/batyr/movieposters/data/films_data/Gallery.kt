package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class Gallery(
    val items: List<Item>,
    val total: Int,
    val totalPages: Int
) {
    @Keep
    data class Item(
        val imageUrl: String,
        val previewUrl: String
    )
}