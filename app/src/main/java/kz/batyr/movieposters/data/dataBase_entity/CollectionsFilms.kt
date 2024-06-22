package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections_name")
data class CollectionsFilms(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "collection_name") val collectionName: String,
)