package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewed_films")



data class ViewedFilmsDbEntity(
    @PrimaryKey (autoGenerate = true) val idInTable:Int = 0,
    @ColumnInfo(name = "collection_name")
    val collectionName:String,
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "name_ru") val nameRu:String?,
    @ColumnInfo(name = "name_en") val nameEn:String?,
    @ColumnInfo(name = "name_original") val nameOriginal:String?,
    @ColumnInfo(name = "genre") val genre:String?,
    @ColumnInfo(name = "previewPosterUrl") val previewPosterUrl:String?,
    @ColumnInfo(name = "rate") val rate:Double?,
)
//    @ColumnInfo(name = "name_en") val nameEn:String?,
//    @ColumnInfo(name = "name_original") val nameOriginal:String,