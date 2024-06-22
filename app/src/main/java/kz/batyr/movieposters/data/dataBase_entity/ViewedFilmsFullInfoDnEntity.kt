package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewed_films_full_info")
data class ViewedFilmsFullInfoDnEntity(
    @PrimaryKey (autoGenerate = true) val idInTable:Int = 0,
    @ColumnInfo(name = "collection_name") val collectionName:String,
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "description") val description:String?,
    @ColumnInfo(name = "short_description") val shortDescription:String?,
    @ColumnInfo(name = "rate_name") val rateName:String?,
    @ColumnInfo(name = "year_genres") val yearGenres:String?,
    @ColumnInfo(name = "country_duration_age_limit") val countryDurationAgeLimit:String?,
    @ColumnInfo(name = "logo_url") val logoUrl:String?,
    @ColumnInfo(name = "poster_url") val posterUrl:String?,
    @ColumnInfo(name = "name_original") val nameOriginal:String?,
    @ColumnInfo(name = "totalSeason") val totalSeason: Int?,
    @ColumnInfo(name = "totalEpisodes") val totalEpisodes: Int?,
    @ColumnInfo(name = "type") val type: String?
)
