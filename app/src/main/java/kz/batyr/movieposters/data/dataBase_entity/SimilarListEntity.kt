package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.batyr.movieposters.data.films_data.FilmListPremiers

@Entity ("similar_list")
data class SimilarListEntity(
    @PrimaryKey(autoGenerate = true) val idInTable:Int = 0,
    @ColumnInfo(name = "collection_name")
    val collectionName:String,
    @ColumnInfo(name = "similarId") val similarId:Int,
    @ColumnInfo(name = "filmId") val filmId:Int,
    @ColumnInfo(name = "name_ru") val nameRu:String?,
    @ColumnInfo(name = "name_en") val nameEn:String?,
    @ColumnInfo(name = "name_original") val nameOriginal:String?,
    @ColumnInfo(name = "previewPosterUrl") val previewPosterUrl:String?,
    @ColumnInfo(name = "rate") val rate:Double?,
)
