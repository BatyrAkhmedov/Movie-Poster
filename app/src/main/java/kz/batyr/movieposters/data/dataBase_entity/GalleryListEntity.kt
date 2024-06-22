package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_list_entity")
data class GalleryListEntity(
    @PrimaryKey(autoGenerate = true) val idInTable:Int = 0,
    @ColumnInfo(name = "collection_name") val collectionName:String,
    @ColumnInfo(name = "filmId") val filmId: Int,
    @ColumnInfo(name = "posterUrl") val posterUrl: String,
    @ColumnInfo(name = "previewUrl") val previewUrl: String,
)
