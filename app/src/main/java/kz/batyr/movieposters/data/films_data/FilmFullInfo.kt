package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class FilmFullInfo(
    val completed: Boolean?,
    val countries: List<Country>?,
    val coverUrl: String?,
    val description: String?,
    val editorAnnotation: Any?,
    val endYear: Any?,
    val filmLength: Int?,
    val genres: List<Genre>?,
    val has3D: Boolean?,
    val hasImax: Boolean?,
    val imdbId: String?,
    val isTicketsAvailable: Boolean?,
    val kinopoiskHDId: String?,
    val kinopoiskId: Int,
    val lastSync: String?,
    val logoUrl: String?,
    val nameEn: String?,
    val nameOriginal: String,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String,
    val productionStatus: Any?,
    val ratingAgeLimits: String?,
    val ratingAwait: Any?,
    val ratingAwaitCount: Int?,
    val ratingFilmCritics: Double?,
    val ratingFilmCriticsVoteCount: Int?,
    val ratingGoodReview: Int?,
    val ratingGoodReviewVoteCount: Int?,
    val ratingImdb: Double?,
    val ratingImdbVoteCount: Int?,
    val ratingKinopoisk: Double?,
    val ratingKinopoiskVoteCount: Int?,
    val ratingMpaa: String?,
    val ratingRfCritics: Any?,
    val ratingRfCriticsVoteCount: Int?,
    val reviewsCount: Int?,
    val serial: Boolean,
    val shortDescription: String?,
    val shortFilm: Boolean?,
    val slogan: String?,
    val startYear: Any?,
    val type: String?,
    val webUrl: String?,
    val year: Int?,
    val totalSeason: Int?,
    val totalEpisodes: Int?
) {
    @Keep
    data class Country(
        val country: String
    )

    @Keep
    data class Genre(
        val genre: String
    )
}