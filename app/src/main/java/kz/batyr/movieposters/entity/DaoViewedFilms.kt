package kz.batyr.movieposters.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kz.batyr.movieposters.data.dataBase_entity.ActorListEntity
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.data.dataBase_entity.GalleryListEntity
import kz.batyr.movieposters.data.dataBase_entity.SimilarListEntity
import kz.batyr.movieposters.data.dataBase_entity.StaffListEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsFullInfoDnEntity

@Dao
interface DaoViewedFilms {

    //collection
    @Insert(entity = CollectionsFilms::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionsFilms)
    @Query("select * from collections_name")
    fun getALlCollections ():Flow<List<CollectionsFilms>>
    @Query("delete from collections_name where collection_name = :collectionName")
    suspend fun deleteCollectionByCollections (collectionName: String)
    @Query("delete from viewed_films where collection_name = :collectionName")
    suspend fun deleteCollectionByViewed (collectionName: String)
    @Query("delete from viewed_films_full_info where collection_name = :collectionName")
    suspend fun deleteCollectionByViewedFullInfo (collectionName: String)
    @Transaction
    suspend fun deleteCollection (collectionName: String) {
        deleteCollectionByCollections(collectionName)
        deleteAllInfoFromCollection(collectionName)
    }
    @Transaction
    suspend fun deleteAllInfoFromCollection (collectionName: String) {
        deleteCollectionByViewed(collectionName)
        deleteCollectionByViewedFullInfo(collectionName)
        deleteActorsFromCollection(collectionName)
        deleteStaffFromCollection(collectionName)
        deleteGalleryFromCollection(collectionName)
        deleteSimilarFromCollection(collectionName)
    }
    @Transaction
    suspend fun deleteFilmFromCollection (collectionName: String, filmId: Int) {
        deleteActorsFromFilm(collectionName, filmId)
        deleteGalleryFromFilm(collectionName, filmId)
        deleteStaffFromFilm(collectionName, filmId)
        deleteSimilarFromFilm(collectionName, filmId)
        deleteFilm(collectionName, filmId)
        deleteFilmFullInfo(collectionName, filmId)
    }


    //films
    @Insert(entity = ViewedFilmsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: ViewedFilmsDbEntity)
//    @Update
//    suspend fun updateFilm (film: ViewedFilmsDbEntity)
    @Query ("delete from viewed_films where collection_name = :collectionName and id = :id")
    suspend fun deleteFilm (collectionName:String, id: Int)
    @Query ("delete from viewed_films where collection_name = :collectionName")
    suspend fun deleteAll (collectionName:String)
    @Query("select * from viewed_films")
    fun getAllFilms(): Flow<List<ViewedFilmsDbEntity>>
    @Query("select * from viewed_films where id = :id")
    fun getFilm(id: Int): ViewedFilmsDbEntity

    //filmsFullInfo
    @Insert(entity = ViewedFilmsFullInfoDnEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(filmFullInfo: ViewedFilmsFullInfoDnEntity)

    @Query ("delete from viewed_films_full_info where collection_name = :collectionName and id = :id")
    suspend fun deleteFilmFullInfo (collectionName:String, id: Int)

    @Query ("delete from viewed_films_full_info where collection_name = :collectionName")
    suspend fun deleteAllFullInfo (collectionName:String)

    @Query("select * from viewed_films_full_info")
    fun getAllFilmsFullInfo(): Flow<List<ViewedFilmsFullInfoDnEntity>>
    @Query("select * from viewed_films_full_info where id = :id")
    fun getFullInfo(id: Int): ViewedFilmsFullInfoDnEntity

    //actorsList
    @Insert (entity = ActorListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors (actorList:List<ActorListEntity>)
    @Query ("select * from actor_list_entity")
    fun getActorList():Flow<List<ActorListEntity>>
    @Query ("delete from actor_list_entity where collection_name = :collectionName and filmId=:filmId")
    suspend fun deleteActorsFromFilm (collectionName: String, filmId: Int)
                    // for interested and customer
    @Query ("delete from actor_list_entity where collection_name = :collectionName")
    suspend fun deleteActorsFromCollection (collectionName: String)

    //staffList
    @Insert (entity = StaffListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff (staffList:List<StaffListEntity>)
    @Query ("select * from staff_list_entity")
    fun getStaffList():Flow <List<StaffListEntity>>
    @Query ("delete from staff_list_entity where collection_name = :collectionName and filmId=:filmId")
    suspend fun deleteStaffFromFilm (collectionName: String, filmId: Int)
                    // for interested and customer
    @Query ("delete from staff_list_entity where collection_name =:collectionName")
    suspend fun deleteStaffFromCollection (collectionName: String)

    //galleryList
    @Insert (entity = GalleryListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGallery (galleryList:List<GalleryListEntity>)
    @Query ("select * from gallery_list_entity")
    fun getGalleryList ():Flow<List<GalleryListEntity>>
    @Query ("delete from gallery_list_entity where collection_name = :collectionName and filmId=:filmId")
    suspend fun deleteGalleryFromFilm (collectionName: String, filmId: Int)
                    // for interested and customer
    @Query ("delete from gallery_list_entity where collection_name =:collectionName")
    suspend fun deleteGalleryFromCollection (collectionName: String)

    //similarList
    @Insert (entity = SimilarListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimilar (similarListL:List<SimilarListEntity>)
    @Query ("select * from similar_list")
    fun getSimilarList (): Flow<List<SimilarListEntity>>
    @Query ("delete from similar_list where collection_name = :collectionName and similarId=:similarId")
    suspend fun deleteSimilarFromFilm (collectionName: String, similarId: Int)
    // for interested and customer
    @Query ("delete from similar_list where collection_name = :collectionName")
    suspend fun deleteSimilarFromCollection (collectionName: String)


}
