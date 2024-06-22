package kz.batyr.movieposters.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.domain.GetFilmsMethods

class PagingGalleryDataSource (val filmId:Int, val type:String): PagingSource<Int, Gallery.Item>()  {
    private val repository = GetFilmsMethods()
    override fun getRefreshKey(state: PagingState<Int, Gallery.Item>): Int? = FIRST_PAGE


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery.Item> {
        val page = params.key ?: FIRST_PAGE
        return when (type) {

            still -> kotlin.runCatching {
                repository.getGallery(filmId, still, page).items
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "SHOOTING" -> kotlin.runCatching {
                repository.getGallery(filmId, shooting, page).items
            }.fold(onSuccess = {
                Log.d("TAG", "page load $it")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) })

            "POSTER" -> kotlin.runCatching {
                repository.getGallery(filmId, poster, page).items
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
        const val still = "STILL"
        const val shooting = "SHOOTING"
        const val poster = "POSTER"
    }
}