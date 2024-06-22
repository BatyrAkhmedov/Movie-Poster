package kz.batyr.movieposters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kz.batyr.movieposters.data.DateListForPremiers
import kz.batyr.movieposters.data.PagingDataSource
import kz.batyr.movieposters.data.PagingGalleryDataSource
import kz.batyr.movieposters.data.dataBase_entity.ActorListEntity
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.data.dataBase_entity.GalleryListEntity
import kz.batyr.movieposters.data.dataBase_entity.SimilarListEntity
import kz.batyr.movieposters.data.dataBase_entity.StaffListEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsFullInfoDnEntity
import kz.batyr.movieposters.data.films_data.FilmFullInfo
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.data.films_data.GenresAndCountriesID
import kz.batyr.movieposters.data.films_data.SeriesInfo
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.data.films_data.StaffInfo
import kz.batyr.movieposters.domain.GetFilmsMethods
import kz.batyr.movieposters.domain.State
import kz.batyr.movieposters.entity.DaoViewedFilms
import java.time.LocalDateTime

class MainViewModel(private val daoFilms:DaoViewedFilms) : ViewModel() {
    private val getFilmsMethods = GetFilmsMethods()


    lateinit var getFilmById: FilmFullInfo
    lateinit var getFilmByIdForActorPage: FilmListPremiers.Item
    lateinit var getActorsList:List<Staff.StaffItem>
    lateinit var getStaffList:List<Staff.StaffItem>
    lateinit var bestFilmsFromActorPage:List<StaffInfo.Film>
    lateinit var tenBestFilms:List<FilmListPremiers.Item>
    lateinit var allFilmsFromActorPage:List<StaffInfo.Film>
    lateinit var random1: GenresAndCountriesID.Genre
    lateinit var random2: GenresAndCountriesID.Genre
    lateinit var pagedRandom1: Flow<PagingData<FilmListPremiers.Item>>
    lateinit var pagedRandom2: Flow<PagingData<FilmListPremiers.Item>>
    lateinit var pagedGalleryStill: Flow<PagingData<Gallery.Item>>
    lateinit var pagedGalleryShooting: Flow<PagingData<Gallery.Item>>
    lateinit var pagedGalleryPoster: Flow<PagingData<Gallery.Item>>
    lateinit var randomGenreAndCountries: Map<Int, Int>
    lateinit var gallery: Gallery
    lateinit var staffInfo:StaffInfo
    var pressedYearFrom:String? = null
    var pressedYearTo:String? = null
    var searchTypeFilm = "ALL"
    var searchSortFilm = "YEAR"
    var searchCountry:String = "Не выбрано"
    var searchGenre:String = "Не выбрано"
    var searchYearFrom:String = "Не выбрано"
    var searchYearTo:String = "Не выбрано"
    var searchSliderMin = 1
    var searchSliderMax = 10
    var totalEpisodes: Int? = null
    var totalSeasons: Int? = null
    var totalSimilars:Int? = null
    var pageSizeForRandom1: Int? = null
    var pageSizeForRandom2: Int? = null
    var dateList = DateListForPremiers().getDateList()


// state
    private val _progressBarState = MutableStateFlow<State>(State.Success)
    val progressBarState = _progressBarState.asStateFlow()

    var buttonEnabledUserFragment = false

//FlowCollection
    val collectionFilmsDB = this.daoFilms.getALlCollections().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val viewedFilmsDB = this.daoFilms.getAllFilms().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val viewedFilmsFullInfoDB = this.daoFilms.getAllFilmsFullInfo().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val actorListDB = this.daoFilms.getActorList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val staffListDB = this.daoFilms.getStaffList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val galleryListDB = daoFilms.getGalleryList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val similarListDB = daoFilms.getSimilarList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _collectionInterested = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val collectionInterested = _collectionInterested.stateIn(viewModelScope, SharingStarted.Eagerly, _collectionInterested.value)



    private val _collectionViewed = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val collectionViewed = _collectionViewed.stateIn(viewModelScope, SharingStarted.Eagerly, _collectionViewed.value)

    private val _selectedCollection = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val selectedCollection = _selectedCollection.stateIn(viewModelScope, SharingStarted.Eagerly, _selectedCollection.value)

    //FlowFilm
    private val _moviesPremiers = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getMoviesPremiers =
        _moviesPremiers.stateIn(viewModelScope, SharingStarted.Eagerly, _moviesPremiers.value)

    private val _topPopular = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getTopPopular =
        _topPopular.stateIn(viewModelScope, SharingStarted.Eagerly, _topPopular.value)

    private val _searchFilms = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val searchFilms = _searchFilms.stateIn(viewModelScope, SharingStarted.Eagerly, _searchFilms.value)

    private val _getTop250 = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getTop250 = _getTop250.stateIn(viewModelScope, SharingStarted.Eagerly, _getTop250.value)

    private val _getSerials = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getSerials = _getSerials.stateIn(viewModelScope, SharingStarted.Eagerly, _getSerials.value)

    private val _getRandom1 = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getRandom1 = _getRandom1.stateIn(viewModelScope, SharingStarted.Eagerly, _getRandom1.value)

    private val _getRandom2 = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val getRandom2 = _getRandom2.stateIn(viewModelScope, SharingStarted.Eagerly, _getRandom2.value)

    private val _genresID = MutableStateFlow<List<GenresAndCountriesID.Genre>>(emptyList())
    val genresID = _genresID.stateIn(viewModelScope, SharingStarted.Eagerly, _genresID.value)

    private val _countriesID = MutableStateFlow<List<GenresAndCountriesID.Country>>(emptyList())
    val countriesID =
        _countriesID.stateIn(viewModelScope, SharingStarted.Eagerly, _countriesID.value)

    private val _seriesInfo = MutableStateFlow<List<SeriesInfo.Item>>(emptyList())
    val seriesInfo = _seriesInfo.stateIn(viewModelScope, SharingStarted.Eagerly, _seriesInfo.value)

    private val _similarsFilm = MutableStateFlow<List<FilmListPremiers.Item>>(emptyList())
    val similarsFilm = _similarsFilm.stateIn(viewModelScope, SharingStarted.Eagerly, _similarsFilm.value)


    val pagedPopular: Flow<PagingData<FilmListPremiers.Item>> = Pager(
        config = PagingConfig(10),
        initialKey = null,
        pagingSourceFactory = { PagingDataSource("Популярное") }
    ).flow.cachedIn(viewModelScope)

    val pagedTop250: Flow<PagingData<FilmListPremiers.Item>> = Pager(
        config = PagingConfig(10),
        initialKey = null,
        pagingSourceFactory = { PagingDataSource("Топ-250") }
    ).flow.cachedIn(viewModelScope)

    val pagedSerials: Flow<PagingData<FilmListPremiers.Item>> = Pager(
        config = PagingConfig(10),
        initialKey = null,
        pagingSourceFactory = { PagingDataSource("Сериалы") }
    ).flow.cachedIn(viewModelScope)



    init {
        loadTop250()
        loadPremiers()
        loadPopular()
        loadSerials()
        loadStandardCollections()
    }
    private fun insertFilmsToFlow (collectionName: String , flow: MutableStateFlow<List<FilmListPremiers.Item>>) {
        val filmList =  viewedFilmsDB.value
        val filteredList = filmList.filter { it.collectionName == collectionName }.map { FilmListPremiers.Item (filmId = it.id, nameRu = it.nameRu,
            genres = listOf(FilmListPremiers.Item.Genre (it.genre)), posterUrlPreview = it.previewPosterUrl, ratingKinopoisk = it.rate,) }
        flow.value = filteredList
    }
    fun deleteCollectionAndAllInfo (collectionName: String) {
        viewModelScope.launch{daoFilms.deleteCollection(collectionName)}
    }
//    suspend fun deleteAllFilmsFromCollection(collectionName: String) {
//        daoFilms.deleteAllInfoFromCollection(collectionName)
//    }
    fun deleteFilmFromCollection (collectionName: String, filmId: Int) {
        viewModelScope.launch {
            daoFilms.deleteFilmFromCollection(collectionName, filmId)
        }
    }

    suspend fun deleteInterestedFilms(){
        daoFilms.deleteAllInfoFromCollection("Вам было интересно")
        _collectionInterested.value = emptyList()
    }
    fun findFilm (collectionName: String, filmId: Int) = viewedFilmsFullInfoDB.value.find {it.collectionName == collectionName &&
                it.id == filmId  }

    fun getSelectedCollections(collectionName: String):Job {
        return viewModelScope.launch {
            insertFilmsToFlow(collectionName, _selectedCollection)
        }
    }

    fun getStandardCollections (): Job {
       return viewModelScope.launch {
            insertFilmsToFlow("Вам было интересно", _collectionInterested)
           insertFilmsToFlow("Просмотрено", _collectionViewed)
        }
    }
    fun insertCollection (collectionName: String) {
        viewModelScope.launch {
            daoFilms.insertCollection(CollectionsFilms(collectionName = collectionName))
        }
    }
    fun insertFilmToCollection (filmDB:ViewedFilmsDbEntity){
        viewModelScope.launch {
            val film = viewedFilmsDB.value.find { it.id == filmDB.id && it.collectionName == filmDB.collectionName}

            //for reverse data
            if (film!=null) {
                daoFilms.deleteFilm(film.collectionName, film.id)
                daoFilms.insertFilm(film)
            }
            else daoFilms.insertFilm(filmDB)
        }
    }
    fun insertFilmFullInfoToCollection (filmDB:ViewedFilmsFullInfoDnEntity ) {
        viewModelScope.launch {
            val film = viewedFilmsFullInfoDB.value.find { it.id == filmDB.id && it.collectionName == filmDB.collectionName}

            if (film!=null) {
                daoFilms.deleteFilmFullInfo(film.collectionName, film.id)
                daoFilms.insertFilm(film)
            }
            else daoFilms.insertFilm(filmDB)
        }
    }

    fun insertActorsFromFilm(actorList:List<ActorListEntity>) {
        viewModelScope.launch {
           daoFilms.insertActors(actorList)
        }
    }
    fun insertStaffFromFilm (staffList:List<StaffListEntity>){
        viewModelScope.launch {
            daoFilms.insertStaff(staffList)
        }
    }
    fun insertGalleryFromFilm (galleryList:List<GalleryListEntity>) {
        viewModelScope.launch {
            daoFilms.insertGallery(galleryList)
        }
    }
    fun insertSimilarFromFilm (similarList:List<SimilarListEntity>) {
        viewModelScope.launch {
            daoFilms.insertSimilar(similarList)
        }
    }

    private fun loadStandardCollections() {
        viewModelScope.launch {
            if (collectionFilmsDB.value.isEmpty()) {
                daoFilms.insertCollection(CollectionsFilms( collectionName = "Любимые"))
                daoFilms.insertCollection(CollectionsFilms( collectionName = "Хочу посмотреть"))
//                daoFilms.insertCollection(CollectionsFilms(collectionName = "Dsad"))
//                daoFilms.insertFilm(ViewedFilmsDbEntity(collectionName = "Dsad", id = 5260016, nameRu = "name", genre = "genre", previewPosterUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp/5260016.jpg",
//                    rate = 2.3, nameEn = "dasd", nameOriginal = "swfsef"))

            }
        }
    }







    fun progressBarEnable (){
        _progressBarState.value = State.Loading
    }
    fun progressBarDisable (){
        _progressBarState.value = State.Success
    }

    suspend fun searchFilm (countries: Array<Int>?,
                            genres: Array<Int>?,
                            order: String = "--",
                            type: String = "--",
                            ratingFrom: Int = 0,
                            ratingTo: Int = 10,
                            page:Int = 1,
                            yearFrom:Int = 1900,
                            yearTo: Int = 3000,
                            keyword:String) {

        kotlin.runCatching {
            getFilmsMethods.getSearchingFilm (countries, genres, order, type, ratingFrom, ratingTo, page, yearFrom, yearTo, keyword)
        }.fold(onSuccess = {
            _searchFilms.value = it.items
        }
            , onFailure = { Log.e("TAG", "OnFailure getSearchingFilms") })


    }

    fun loadStaffInfo (staffId:Int): Job {
        return viewModelScope.launch {
            staffInfo = getFilmsMethods.getStaffInfo (staffId)
        }
    }

    fun loadTypeGallery (filmId: Int) {
        Log.d("TAG", "VM loadTyoeGallery")
        pagedGalleryStill = Pager(
            config = PagingConfig(10),
            initialKey = null,
            pagingSourceFactory = { PagingGalleryDataSource(filmId,"STILL") }
        ).flow.cachedIn(viewModelScope)
        pagedGalleryShooting = Pager(
            config = PagingConfig(10),
            initialKey = null,
            pagingSourceFactory = { PagingGalleryDataSource(filmId,"SHOOTING") }
        ).flow.cachedIn(viewModelScope)
        pagedGalleryPoster = Pager(
            config = PagingConfig(10),
            initialKey = null,
            pagingSourceFactory = { PagingGalleryDataSource(filmId,"POSTER") }
        ).flow.cachedIn(viewModelScope)
    }

    suspend fun loadGallery (filmId: Int): Job {
       return viewModelScope.launch {
           gallery = getFilmsMethods.getGallery(filmId)
        }
    }
    suspend fun loadSerialInfo (serialId:Int): Job {

        val job = viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getSerialInfo(serialId)
            }.fold(onSuccess = {
                _seriesInfo.value = it.items
                totalSeasons = it.total
                totalEpisodes = it.items.firstOrNull()?.episodes?.lastOrNull()?.episodeNumber
                Log.e("TAG", "SeriesInfo ${it.items}")
            }, onFailure = { Log.e("TAG", "OnFailure getSerialInfo") })
        }
        return job
    }

    suspend fun loadParticipants(filmId:Int): Job {
        return viewModelScope.launch {
            val participants = getFilmsMethods.getStaffFromFilmId(filmId)
            getActorsList = participants.filter { it.professionKey == "ACTOR" }
            getStaffList = participants.filter { it.professionKey != "ACTOR" }
            Log.d("TAG", "Actors $getActorsList")
            Log.d("TAG", "Staff $getStaffList")
        }
    }

    suspend fun loadFilmById(id: Int): Job {
        return viewModelScope.launch {
            getFilmById = getFilmsMethods.getID(id)
        }
    }
    suspend fun loadFilmByIdForActorPage(id: Int): Job {
        return viewModelScope.launch {
            getFilmByIdForActorPage = getFilmsMethods.getFilmFromIdForActorPage (id)
        }
    }

    fun loadDataToPagedRandom1(countries: Int, genre: Int) {
        pagedRandom1 = Pager(
            config = PagingConfig(pageSizeForRandom1!!),
            initialKey = null,
            pagingSourceFactory = { PagingDataSource("Random1", countries, genre) }
        ).flow.cachedIn(viewModelScope)
    }

    fun loadDataToPagedRandom2(countries: Int, genre: Int) {
        pagedRandom2 = Pager(
            config = PagingConfig(pageSizeForRandom2!!),
            initialKey = null,
            pagingSourceFactory = { PagingDataSource("Random2", countries, genre) }
        ).flow.cachedIn(viewModelScope)
    }


    suspend fun loadGenresAndCountriesId() {
        val job = viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getGenresAndCountries()
            }.fold(onSuccess = {
                _genresID.value = it.genres
                _countriesID.value = it.countries
                Log.e("TAG", "Genres ${it.genres}")
            }, onFailure = { Log.e("TAG", "OnFailure getGenres") })
        }
        job.join()
    }

    fun loadRandomFilms(firstGenre: Int, firstCountry: Int, secondGenre: Int, secondCountry: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getSerialsAndRandomGenres(
                    arrayOf(firstCountry),
                    arrayOf(firstGenre),
                    "ALL"
                )
            }.fold(onSuccess = {
                _getRandom1.value = it.items
                pageSizeForRandom1 = it.pagesCount
                Log.e("TAG", "${it.items}")
            }, onFailure = { Log.e("TAG", "OnFailure") })
        }
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getSerialsAndRandomGenres(
                    arrayOf(secondCountry),
                    arrayOf(secondGenre),
                    "ALL"
                )
            }.fold(onSuccess = {
                _getRandom2.value = it.items
                pageSizeForRandom2 = it.pagesCount
                Log.e("TAG", "${it.items}")
            }, onFailure = { Log.e("TAG", "OnFailure") })
        }
    }

    fun loadSerials() {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getSerialsAndRandomGenres(null, null, "TV_SERIES", 2014)
            }.fold(onSuccess = {
                _getSerials.value = it.items
                Log.e("TAG", "${it.items}")
            }, onFailure = { Log.e("TAG", "OnFailure") })
        }
    }

    fun loadTop250() {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getTop()
            }.fold(onSuccess = {
                _getTop250.value = it.films
                Log.e("TAG", "${it.films}")
            }, onFailure = { Log.e("TAG", "OnFailure") })
        }
    }

    fun loadPopular() {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getPopular()
            }.fold(onSuccess = {
                _topPopular.value = it.films
            }, onFailure = { Log.e("TAG", "OnFailure") })
        }
    }

    fun loadPremiers() {
        val dateList = DateListForPremiers().getDateList()
        Log.e("TAG", dateList.toString())
        val currentDate = LocalDateTime.now()
        val year = currentDate.year
        val month = currentDate.month.name
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                getFilmsMethods.getPremiers(year, month)
            }.fold(
                onSuccess = {
                    _moviesPremiers.value =
                        it.items.filter { movie -> dateList.contains(movie.premiereRu) }
                    Log.e("TAG", "onSuccess ${dateList}")
                },
                onFailure = { Log.e("TAG", "OnFailure") }
            )
        }
    }
    fun loadSimilars (filmId: Int){
        viewModelScope.launch {
            kotlin.runCatching {
                getFilmsMethods.getSimilars(filmId)
            }.fold(onSuccess = {
                _similarsFilm.value = it.items
                totalSimilars = it.total
            }, onFailure = {
                Log.e("TAG", "OnFailure")
            })
        }
    }


}