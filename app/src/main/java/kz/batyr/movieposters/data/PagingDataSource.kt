package kz.batyr.movieposters.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.GenresAndCountriesID
import kz.batyr.movieposters.domain.GetFilmsMethods

class PagingDataSource(private val filmCategory:String,  val countries:Int = -1,  val genre:Int = -1): PagingSource<Int, FilmListPremiers.Item>() {
    private val repository = GetFilmsMethods()
    override fun getRefreshKey(state: PagingState<Int, FilmListPremiers.Item>): Int? = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmListPremiers.Item> {
        val page = params.key ?: 1
        return when (filmCategory) {

            "Популярное" -> kotlin.runCatching {
                repository.getPopular(page).films
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "Топ-250" -> kotlin.runCatching {
                repository.getTop(page).films
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "Сериалы" -> kotlin.runCatching {
                repository.getSerialsAndRandomGenres(null, null, "TV_SERIES", 2014, page).items
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "Random1" -> kotlin.runCatching {
                repository.getSerialsAndRandomGenres(arrayOf(countries), arrayOf(genre), "ALL", page).items
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "Random2" -> kotlin.runCatching {
                repository.getSerialsAndRandomGenres(arrayOf(countries), arrayOf(genre), "ALL", page).items
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            else -> throw (Exception("load Failed"))
        }

    }
    companion object {
        const val FIRST_PAGE = 1
    }
}