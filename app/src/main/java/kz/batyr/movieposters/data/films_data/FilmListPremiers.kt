package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class FilmListPremiers(
    val items: List<Item>,
    val total: Int?,
    val films: List<Item>,
    val pagesCount: Int,
    val totalPages: Int
) {
    @Keep
    data class Item(
        val countries: List<Country>?=null,
        val duration: Int?=null,
        val genres: List<Genre>?,
        val kinopoiskId: Int?=null,
        val nameEn: String?=null,
        val nameRu: String?,
        val posterUrl: String?=null,
        val posterUrlPreview: String?,
        val premiereRu: String?=null,
        val year: Int?=null,
        val rating: String?=null,
        val filmId: Int,
        val filmLength: String?=null,
        val isAfisha: Int?=null,
        val isRatingUp: Any?=null,
        val ratingChange: Any?=null,
        val ratingVoteCount: Int?=null,
        val imdbId: String?=null,
        val nameOriginal: String?=null,
        val ratingImdb: Double?=null,
        val ratingKinopoisk: Double?=null,
        val type: String?=null,
    ) {
        @Keep
        data class Country(
            val country: String?
        )

        @Keep
        data class Genre(
            val genre: String?
        )
    }
}