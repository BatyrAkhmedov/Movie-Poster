package kz.batyr.movieposters.presentation.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.dataBase_entity.ActorListEntity
import kz.batyr.movieposters.data.dataBase_entity.GalleryListEntity
import kz.batyr.movieposters.data.dataBase_entity.SimilarListEntity
import kz.batyr.movieposters.data.dataBase_entity.StaffListEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsFullInfoDnEntity
import kz.batyr.movieposters.data.films_data.FilmFullInfo
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.databinding.FragmentFilmFullInfoBinding
import kz.batyr.movieposters.presentation.FilmFullInfoFragmentArgs
import kz.batyr.movieposters.presentation.FilmFullInfoFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.GalleryAdapter
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ListFilmsAdapter
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ParticipantsAdapter
import java.io.File
import java.io.FileOutputStream


class FilmFullInfoFragment () : Fragment() {
    private val args: FilmFullInfoFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var vb: FragmentFilmFullInfoBinding
    private lateinit var mainJob: Job
    private lateinit var imageUri: Uri
    private val adapterForActors = ParticipantsAdapter { staffId -> onStaffClick(staffId) }
    private val adapterForStaff = ParticipantsAdapter { staffId -> onStaffClick(staffId) }
    private val adapterForGallery = GalleryAdapter()
    private val adapterForSimilars = ListFilmsAdapter({ film -> onFilmClick(film) }, "null")
        private var foundFilm: ViewedFilmsDbEntity? = null
        private var foundFilmFullInfo: ViewedFilmsFullInfoDnEntity? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        vb = FragmentFilmFullInfoBinding.inflate(inflater, container, false)
        return vb.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //icon state


        if (viewModel.findFilm(getString(R.string.likedFilms), args.filmId)!=null) vb.iconLiked.isSelected = true
        if (viewModel.findFilm(getString(R.string.favorites_films), args.filmId)!=null) vb.iconFavorite.isSelected = true
        if (viewModel.findFilm(getString(R.string.viewed_films), args.filmId)!=null) vb.iconViewed.isSelected = true

        vb.recyclerViewActors.adapter = adapterForActors
        vb.recyclerViewWorkers.adapter = adapterForStaff
        vb.recyclerViewGallery.adapter = adapterForGallery
        vb.recyclerViewSimilarMovies.adapter = adapterForSimilars
        Log.e("TAG", "ID${args.filmId}")
        foundFilmFullInfo =
            viewModel.viewedFilmsFullInfoDB.value.find { it.id == args.filmId }
        foundFilm = viewModel.viewedFilmsDB.value.find { it.id == args.filmId }
        val foundActors = viewModel.actorListDB.value.filter { it.filmId == args.filmId}.distinctBy { it.staffId }
        val foundStaff = viewModel.staffListDB.value.filter { it.filmId == args.filmId }.distinctBy { it.staffId }
        val foundGallery = viewModel.galleryListDB.value.filter { it.filmId == args.filmId }.distinctBy { it.posterUrl }
        val foundSimilar = viewModel.similarListDB.value.filter { it.similarId == args.filmId }.distinctBy { it.similarId }
        ////////////////




        when (foundFilmFullInfo) {
            null -> {
                viewModel.loadSimilars(args.filmId)
                viewModel.similarsFilm.onEach {
                    adapterForSimilars.setData(it)
                }.launchIn(lifecycleScope)
                mainJob = viewLifecycleOwner.lifecycleScope.launch {
                    val job = viewModel.loadFilmById(args.filmId)
                    job.join()
                    val job2 = viewModel.loadParticipants(args.filmId)
                    job2.join()
                    val job3 = viewModel.loadGallery(args.filmId)
                    job3.join()
                    val job4 =  viewModel.loadSerialInfo(args.filmId)
                    job4.join()

                    vb.numbersOfSimilarMovies.text = viewModel.totalSimilars.toString()
                    val film = viewModel.getFilmById

                    Log.e("TAGS", "${viewModel.getFilmById}")
                    with(film) {
                        val rateName = "${ratingKinopoisk ?: ratingImdb ?: 0.0} ${nameRu ?: nameOriginal}"
                        val yearGenres = "$year, ${genres?.map { it.genre }?.joinToString(", ")}"
                        val ageLimit = if (ratingAgeLimits != null && ratingAgeLimits.length == 5)
                            "${ratingAgeLimits.takeLast(2)}+"
                        else if (ratingAgeLimits != null && ratingAgeLimits.length == 4)
                            "${ratingAgeLimits.takeLast(1)}+"
                        else "0+"
                        val countryDurationAgeLimit =
                            "${countries?.map { it.country }?.joinToString(", ")}, $ageLimit"

                        setDataFromFilm(posterUrl!!, description, shortDescription, rateName, yearGenres,
                            countryDurationAgeLimit, nameOriginal, type!!, logoUrl, viewModel.totalSeasons, viewModel.totalEpisodes,

                        )

                    }
                    adapterForActors.setData(viewModel.getActorsList)
                    adapterForStaff.setData(viewModel.getStaffList)
                    adapterForGallery.setData(viewModel.gallery.items)
                    vb.numbersOfActors.text = "${viewModel.getActorsList.count()}"
                    vb.numbersOfWorkers.text = "${viewModel.getStaffList.count()}"
                    vb.numbersOfImage.text = "${viewModel.gallery.total}"
                }

            }

            else -> {
                with(foundFilmFullInfo) {
                    setDataFromFilm(
                        this!!.posterUrl!!,
                        description,
                        shortDescription,
                        rateName!!,
                        yearGenres!!,
                        countryDurationAgeLimit!!,
                        nameOriginal,
                        type!!,
                        logoUrl,
                        totalSeason,
                        totalEpisodes
                    )
                }
                adapterForActors.setData(changeActorsType(foundActors))
                adapterForStaff.setData(changeStaffType(foundStaff))
                adapterForGallery.setData(changeGalleryType(foundGallery))
                adapterForSimilars.setData(changeSimilarType(foundSimilar))
                vb.numbersOfActors.text = foundActors.size.toString()
                vb.numbersOfWorkers.text = foundStaff.size.toString()
                vb.numbersOfImage.text = foundGallery.size.toString()
                vb.numbersOfSimilarMovies.text = foundSimilar.size.toString()
                viewModel.getActorsList = (changeActorsType(foundActors))
                viewModel.getStaffList = (changeStaffType(foundStaff))
                Log.e("TAG", "${foundSimilar.joinToString(", ")}")
                Log.e("TAG", "Set Data from Database")
            }
        }




        // load info to Collections
        viewModel.viewModelScope.launch {
            kotlin.runCatching {
                mainJob.join()
            }

            insertAllInfoToCollection(getString(R.string.interestedFilms))

            }











        // Listeners
        vb.iconLiked.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) insertAllInfoToCollection("Любимые")
            else viewModel.deleteFilmFromCollection("Любимые", args.filmId)
        }
        vb.iconFavorite.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) insertAllInfoToCollection(getString(R.string.favorites_films))
            else viewModel.deleteFilmFromCollection(getString(R.string.favorites_films), args.filmId)
        }
        vb.iconViewed.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) insertAllInfoToCollection(getString(R.string.viewed_films))
            else viewModel.deleteFilmFromCollection(getString(R.string.viewed_films), args.filmId)
        }
        vb.iconShare.setOnClickListener {
            val filmName = foundFilm?.nameRu ?: foundFilm?.nameEn ?: viewModel.getFilmById.nameRu ?: viewModel.getFilmById.nameEn

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, " Я хотел бы поделиться с тобой фильмом: \n $filmName \n " +
            "https://www.kinopoisk.ru/film/${viewModel.getFilmById.kinopoiskId}")
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
        }
        vb.iconShowMore.setOnClickListener {
            val bottomDialog = BottomDialogFragment(args.filmId, {collection -> insertAllInfoToCollection(collection)}) {
                updateStateIcons()
            }
         bottomDialog.show(childFragmentManager, "BottomSheetDialog")
        }

        vb.buttonGallery.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToGalleryFragment(args.filmId)
            findNavController().navigate(action)
        }
        vb.numbersOfImage.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToGalleryFragment(args.filmId)
            findNavController().navigate(action)
        }
        vb.numbersOfActors.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToActorsListFragment(
                    getString(
                        R.string.listActors
                    )
                )
            findNavController().navigate(action)
        }
        vb.numbersOfWorkers.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToActorsListFragment(
                    getString(
                        R.string.listStaff
                    )
                )
            findNavController().navigate(action)
        }
        vb.numbersOfSimilarMovies.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToFilmFragment(getString(R.string.similars))
            findNavController().navigate(action)
        }
        vb.showMoreAboutSerials.setOnClickListener {
            val action =
                FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToSeasonFragment("${viewModel.getFilmById.nameRu}")
            findNavController().navigate(action)
        }
        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    // fun
    private fun updateStateIcons() {
        vb.iconLiked.isSelected = viewModel.findFilm(getString(R.string.likedFilms), args.filmId)!=null
        vb.iconFavorite.isSelected = viewModel.findFilm(getString(R.string.favorites_films), args.filmId)!=null
    }
        private fun insertActors (collectionName: String, filmId: Int) {
        viewModel.viewModelScope.launch {
            val actorsList = viewModel.getActorsList
            val newList = actorsList.map {
                ActorListEntity(
                    collectionName = collectionName,
                    filmId = filmId,
                    staffId = it.staffId,
                    nameEn = it.nameEn,
                    nameRu = it.nameRu,
                    description = it.description,
                    posterUrl = it.posterUrl
                )
            }
            viewModel.insertActorsFromFilm(newList)
        }
    }
        private fun insertStaff (collectionName: String, filmId: Int) {
            val staffList = viewModel.getStaffList
            val newList = staffList.map {
                StaffListEntity (
                    collectionName = collectionName,
                    filmId = filmId,
                    profession = it.professionText,
                    nameRu = it.nameRu,
                    nameEn = it.nameEn,
                    posterUrl = it.posterUrl,
                    staffId = it.staffId
                        )
            }
            viewModel.insertStaffFromFilm(newList)
        }
        private fun insertGallery (collectionName: String, filmId: Int) {
            val gallery = viewModel.gallery.items.map {
                GalleryListEntity (
                    collectionName = collectionName,
                    filmId = filmId,
                    previewUrl = it.previewUrl,
                    posterUrl = it.imageUrl
                        )
            }
            viewModel.insertGalleryFromFilm(gallery)

        }
        private fun insertSimilars (collectionName: String, filmId: Int) {
            val similarList = viewModel.similarsFilm.value.map {
                SimilarListEntity (
                    collectionName = collectionName,
                    similarId = filmId ,
                    filmId = it.filmId,
                    nameRu = it.nameRu,
                    nameEn = it.nameEn,
                    nameOriginal = it.nameOriginal,
                    previewPosterUrl = it.posterUrlPreview,
                    rate = it.ratingKinopoisk?:it.ratingImdb
                        )

            }
            viewModel.insertSimilarFromFilm(similarList)
        }
        private fun changeActorsType (actorListDB:List<ActorListEntity>)  = actorListDB.map { Staff.StaffItem (
            description = it.description, nameEn = it.nameEn, nameRu = it.nameRu,posterUrl= it.posterUrl, staffId = it.staffId)
        }.distinctBy { it.staffId }
        private fun changeStaffType (staffListDB:List<StaffListEntity>) = staffListDB.map { Staff.StaffItem (
            nameEn = it.nameEn,
            nameRu = it.nameRu,
            posterUrl = it.posterUrl,
            staffId = it.staffId,
            professionText = it.profession
                )
        }.distinctBy { it.staffId }
        private fun changeGalleryType (galleryListDB:List<GalleryListEntity>) = galleryListDB.map {
            Gallery.Item (
                imageUrl = it.posterUrl,
                previewUrl = it.previewUrl
                    )
        }.distinctBy { it.imageUrl }
        private fun changeSimilarType (similarListDB:List<SimilarListEntity>) = similarListDB.map {
            FilmListPremiers.Item (
                filmId = it.filmId,
                nameOriginal = it.nameOriginal,
                nameEn = it.nameEn,
                nameRu = it.nameRu,
                genres = null,
                ratingKinopoisk = it.rate,
                posterUrlPreview = it.previewPosterUrl
                    )
        }.distinctBy { it.filmId }
        private fun complexLoadFilmToCollection (collectionName: String, foundFilm: ViewedFilmsDbEntity, foundFilmFullInfo: ViewedFilmsFullInfoDnEntity) {
        with(foundFilm) {
            loadFilmToCollection(collectionName, nameRu, nameEn, nameOriginal, id,
                genre, previewPosterUrl!!, rate
            )
        }
        with(foundFilmFullInfo) {
                loadFilmFullInfoToCollection(collectionName, id, posterUrl!!, logoUrl, description,
                shortDescription, totalSeason, type!!, rateName!!, nameOriginal, countryDurationAgeLimit!!, yearGenres!!, totalEpisodes)
        }
    }
        private fun complexLoadFilmToCollection (collectionName: String, film: FilmFullInfo){
        with (film) {
            val rate = ratingImdb ?: ratingKinopoisk
            val rateName = "${ratingKinopoisk ?: ratingImdb ?: 0.0} ${nameRu ?: nameOriginal}"
            val yearGenres = "$year, ${genres?.map { it.genre }?.joinToString(", ")}"
            val ageLimit = if (ratingAgeLimits != null && ratingAgeLimits.length == 5)
                "${ratingAgeLimits.takeLast(2)}+"
            else if (ratingAgeLimits != null && ratingAgeLimits.length == 4)
                "${ratingAgeLimits.takeLast(1)}+"
            else "0+"
            val countryDurationAgeLimit =
                "${countries?.map { it.country }?.joinToString(", ")}, $ageLimit"
            loadFilmToCollection(collectionName, nameRu, nameEn, nameOriginal, kinopoiskId,
                genres?.firstOrNull()?.genre, posterUrlPreview, rate)
            loadFilmFullInfoToCollection(collectionName, kinopoiskId, posterUrl!!, logoUrl, description, shortDescription, viewModel.totalSeasons, type!!,
                rateName, nameOriginal, countryDurationAgeLimit, yearGenres, viewModel.totalEpisodes)
        }
    }
        private fun insertAllInfoToCollection(collectionName: String) {
            if (foundFilmFullInfo != null && foundFilm != null)
                complexLoadFilmToCollection(collectionName ,
                    foundFilm!!, foundFilmFullInfo!!
                )
            else {
                complexLoadFilmToCollection(collectionName,viewModel.getFilmById)
                insertActors(collectionName, args.filmId)
                insertStaff(collectionName, args.filmId)
                insertGallery(collectionName, args.filmId)
                insertSimilars(collectionName, args.filmId)
            }
        }

    private fun setDataFromFilm(posterUrl: String, description: String?, shortDescription: String?, rateName: String, yearGenres: String,
        countryDurationAgeLimit: String, nameOriginal: String?, type: String, logoUrl: String?, totalSeason: Int?, totalEpisodes: Int?,
    ) {
        Glide.with(vb.poster.context)
            .asBitmap()
            .load (posterUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val tempFile = File.createTempFile("temp_image", ".jpg")
                    val outputStream = FileOutputStream(tempFile)
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()

                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "kz.batyr.movieposters.fileprovider",
                        tempFile
                    )
                    imageUri = uri
                    // Используйте imageUri в shareIntent
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Обработка ошибок
                }
            })



        Glide.with(vb.poster.context)
            .load(posterUrl)
            .into(vb.poster)
        vb.description.text = description
        vb.shortDescription.text = shortDescription
        vb.rateName.text = rateName
        vb.yearGenres.text = yearGenres
        vb.countryDurationAgeLimit.text = countryDurationAgeLimit
        if (logoUrl != null) Glide.with(vb.logo.context)
            .load(logoUrl)
            .into(vb.logo)
        else {
            vb.logo.visibility = View.GONE
            vb.nameOriginal.visibility = View.VISIBLE
            vb.nameOriginal.text = nameOriginal
        }
        if (type != "TV_SERIES") vb.constraintSerials.visibility = View.GONE
        else vb.totalSeasonSeries.text = "$totalSeason сезон, $totalEpisodes серий"
    }

    private fun loadFilmToCollection(
        collectionName: String, nameRu: String?, nameEn: String?, nameOriginal: String?,
        filmId: Int, genre: String?, posterUrlPreview: String, rate: Double?,
    ) {
        viewModel.insertFilmToCollection(ViewedFilmsDbEntity(
            collectionName = collectionName,
            id = filmId,
            nameRu = nameRu,
            nameEn = nameEn,
            nameOriginal = nameOriginal,
            genre = genre,
            previewPosterUrl = posterUrlPreview,
            rate = rate
        )
        )
    }

    private fun loadFilmFullInfoToCollection(
        collectionName: String,
        filmId: Int,
        posterUrl: String,
        logoUrl: String?,
        description: String?,
        shortDescription: String?,
        totalSeason: Int?,
        type: String,
        rateName: String,
        nameOriginal: String?,
        countryDurationAgeLimit: String,
        yearGenres: String,
        totalEpisodes: Int?,
    ) {
        viewModel.insertFilmFullInfoToCollection( ViewedFilmsFullInfoDnEntity(
            collectionName = collectionName,
            id = filmId,
            rateName = rateName,
            description = description,
            shortDescription = shortDescription,
            yearGenres = yearGenres,
            countryDurationAgeLimit = countryDurationAgeLimit,
            logoUrl = logoUrl,
            posterUrl = posterUrl,
            nameOriginal = nameOriginal,
            type = type,
            totalEpisodes = totalEpisodes,
            totalSeason = totalSeason))

    }



    private fun onStaffClick(id: Int) {
        val action = FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentToActorFragment(id)
        findNavController().navigate(action)
    }

    private fun onFilmClick(id: Int) {
        val action = FilmFullInfoFragmentDirections.actionFilmFullInfoFragmentSelf(id)
        findNavController().navigate(action)
    }
}

