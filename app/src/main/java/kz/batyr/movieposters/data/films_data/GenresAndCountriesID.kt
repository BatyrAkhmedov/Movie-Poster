package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class GenresAndCountriesID(
    val countries: List<Country>,
    val genres: List<Genre>
) {
    @Keep
    data class Country(
        val country: String,
        val id: Int
    )

    @Keep
    data class Genre(
        val genre: String,
        val id: Int
    )
}