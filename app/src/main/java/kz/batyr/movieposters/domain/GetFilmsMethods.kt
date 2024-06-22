package kz.batyr.movieposters.domain

import android.util.Log
import kz.batyr.movieposters.data.films_data.FilmFullInfo
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.data.films_data.GenresAndCountriesID
import kz.batyr.movieposters.data.films_data.SeriesInfo
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.data.films_data.StaffInfo
import kz.batyr.movieposters.entity.RetrofitAPI
import retrofit2.http.Query

class GetFilmsMethods {
    private val retrofit = RetrofitAPI.createRetrofit()
    suspend fun getID(filmId: Int): FilmFullInfo {
        return retrofit.getFilmId(filmId)
    }

    suspend fun getFilmFromIdForActorPage(filmId: Int): FilmListPremiers.Item {
        return retrofit.getFilmIdForBest(filmId)
    }

    suspend fun getGenresAndCountries(): GenresAndCountriesID {
        return retrofit.getGenresAndCountries()
    }

    suspend fun getPremiers(year: Int, month: String): FilmListPremiers {
        return retrofit.getPremiers(year, month)
    }

    suspend fun getPopular(page: Int = 1): FilmListPremiers {
        return retrofit.getPopular(page = page)
    }

    suspend fun getTop(page: Int = 1): FilmListPremiers {
        return retrofit.getTop250(page = page)
    }

    suspend fun getSerialsAndRandomGenres(
        countries: Array<Int>?,
        genres: Array<Int>?,
        type: String,
        yearFrom: Int = 1900,
        page: Int = 1
    ): FilmListPremiers {
        return retrofit.getSerialsAndRandomGenres(countries, genres, page, type, yearFrom)
    }

    suspend fun getStaffFromFilmId(filmId: Int): List<Staff.StaffItem> {
        val a = retrofit.getStaffFromFilmId(filmId)
        Log.d("TAG", "staff: $a")
        return a
    }

    suspend fun getStaffInfo(staffId: Int): StaffInfo {
        val a = retrofit.getStaff(staffId)
        Log.d("TAG", "staff: $a")
        return a
    }

    suspend fun getSerialInfo(serialId: Int): SeriesInfo {
        return retrofit.getSerialInfo(serialId)
    }

    suspend fun getGallery(filmId: Int, type: String? = null, page: Int = 1): Gallery {
        return retrofit.getGallery(filmId, type, page)
    }

    suspend fun getSimilars(filmId: Int): FilmListPremiers {
        return retrofit.getSimilars(filmId)
    }

    suspend fun getSearchingFilm(
        countries: Array<Int>?,
        genres: Array<Int>?,
        order: String ,
        type: String  ,
        ratingFrom: Int,
        ratingTo: Int,
        page:Int,
        yearFrom:Int,
        yearTo: Int,
        keyword:String
    ): FilmListPremiers {
        return retrofit.getSearchingFilm(countries, genres, order, type, ratingFrom, ratingTo, page, yearFrom, yearTo, keyword)
//        countries = countries,
//        keyword = "Das",
//        order = null,
//        genres = null

    }


}