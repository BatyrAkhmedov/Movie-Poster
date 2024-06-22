package kz.batyr.movieposters.data

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.batyr.movieposters.data.dataBase_entity.ActorListEntity
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.data.dataBase_entity.GalleryListEntity
import kz.batyr.movieposters.data.dataBase_entity.SimilarListEntity
import kz.batyr.movieposters.data.dataBase_entity.StaffListEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsFullInfoDnEntity
import kz.batyr.movieposters.entity.DaoViewedFilms

@Database(
    version = 1,
    entities = [
        ViewedFilmsDbEntity::class,
        ViewedFilmsFullInfoDnEntity::class,
        CollectionsFilms::class,
        ActorListEntity::class,
        StaffListEntity::class,
        GalleryListEntity::class,
        SimilarListEntity::class
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDaoViewedFilms(): DaoViewedFilms

}