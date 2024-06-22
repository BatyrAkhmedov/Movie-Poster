package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class StaffInfo(
    val age: Int,
    val birthday: Any,
    val birthplace: String,
    val death: Any,
    val deathplace: Any,
    val facts: List<Any>,
    val films: List<Film>,
    val growth: Int,
    val hasAwards: Int,
    val nameEn: String,
    val nameRu: String?,
    val personId: Int,
    val posterUrl: String,
    val profession: String,
    val sex: String,
    val spouses: List<Any>,
    val webUrl: String
) {
    @Keep
    data class Film(
        val description: String,
        val filmId: Int,
        val general: Boolean?,
        val nameEn: String,
        val nameRu: String?,
        val professionKey: String,
        val rating: String?
    )
}