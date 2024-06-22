package kz.batyr.movieposters.data.films_data


import androidx.annotation.Keep

@Keep
data class Staff( val staffList: List<StaffItem>) {
    @Keep
    data class StaffItem(
        val description: String? = null,
        val nameEn: String,
        val nameRu: String?,
        val posterUrl: String,
        val professionKey: String? = null,
        val professionText: String? = null,
        val staffId: Int
    )
}