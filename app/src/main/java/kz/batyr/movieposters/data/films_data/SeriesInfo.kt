package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class SeriesInfo(
    val items: List<Item>,
    val total: Int
) {
    @Keep
    data class Item(
        val episodes: List<Episode>,
        val number: Int
    ) {
        @Keep
        data class Episode(
            val episodeNumber: Int,
            val nameEn: String,
            val nameRu: String,
            val releaseDate: String,
            val seasonNumber: Int,
            val synopsis: String
        )
    }
}