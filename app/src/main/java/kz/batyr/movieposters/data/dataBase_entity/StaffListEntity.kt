package kz.batyr.movieposters.data.dataBase_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "staff_list_entity")
data class StaffListEntity(
    @PrimaryKey(autoGenerate = true) val idInTable:Int = 0,
    @ColumnInfo(name = "collection_name") val collectionName:String,
    @ColumnInfo(name = "filmId") val filmId: Int,
    @ColumnInfo(name = "profession") val profession: String?,
    @ColumnInfo(name = "nameEn") val nameEn: String,
    @ColumnInfo(name = "nameRu") val nameRu: String?,
    @ColumnInfo(name = "posterUrl") val posterUrl: String,
    @ColumnInfo(name = "staffId") val staffId: Int
)
