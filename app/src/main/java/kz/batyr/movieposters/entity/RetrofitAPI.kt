package kz.batyr.movieposters.entity

import kz.batyr.movieposters.data.films_data.FilmFullInfo
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.data.films_data.GenresAndCountriesID
import kz.batyr.movieposters.data.films_data.SeriesInfo
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.data.films_data.StaffInfo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAPI {
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/{id}")
    suspend fun getFilmId (@Path("id") filmId:Int): FilmFullInfo
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/{id}")
    suspend fun getFilmIdForBest (@Path("id") filmId:Int): FilmListPremiers.Item
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/filters")
    suspend fun getGenresAndCountries (): GenresAndCountriesID
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/premieres")
    suspend fun getPremiers (@Query("year") year:Int, @Query ("month") month:String): FilmListPremiers
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/top")
    suspend fun getPopular (@Query("type") type:String ="TOP_100_POPULAR_FILMS", @Query ("page") page:Int): FilmListPremiers
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/top")
    suspend fun getTop250 (@Query("type") type:String ="TOP_250_BEST_FILMS", @Query ("page") page:Int): FilmListPremiers
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films?order=RATING&ratingFrom=8&ratingTo=10")
    suspend fun getSerialsAndRandomGenres (@Query ("countries") countries:Array<Int>?, @Query ("genres") genres:Array<Int>? , @Query ("page") page:Int = 1, @Query ("type") type:String, @Query ("yearFrom") yearFrom:Int = 1900
    ): FilmListPremiers
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v1/staff")
    suspend fun getStaffFromFilmId ( @Query ("filmId") filmId:Int): List<Staff.StaffItem>
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/{id}/seasons")
    suspend fun getSerialInfo ( @Path ("id") filmId:Int): SeriesInfo
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/{id}/images")
    suspend fun getGallery ( @Path ("id") filmId:Int, @Query ("type") type: String?,@Query ("page") page: Int): Gallery
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v1/staff/{id}")
    suspend fun getStaff ( @Path ("id") staffId:Int): StaffInfo
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars ( @Path ("id") filmId:Int): FilmListPremiers
    @Headers ("X-API-KEY: $API_KEY")
    @GET ("/api/v2.2/films?order=RATING&ratingFrom=8&ratingTo=10")
    suspend fun getSearchingFilm (@Query ("countries") countries:Array<Int>?,
                                  @Query ("genres") genres:Array<Int>? ,
                                  @Query ("order") order:String,
                                  @Query ("type") type:String,
                                  @Query ("ratingFrom") ratingFrom:Int,
                                  @Query ("ratingTo") ratingTo:Int,
                                  @Query ("page") page:Int,
                                  @Query ("yearFrom") yearFrom:Int,
                                  @Query ("yearTo") yearTo:Int,
                                  @Query ("keyword") keyword:String
    ): FilmListPremiers



    companion object {
        const val API_KEY2 = "78697973-51fe-4ecd-8247-fbfb56280f96"
        const val API_KEY3 = "5f8af297-b130-4202-b20c-63a52a8fc711"
        const val API_KEY = "649630ce-acdb-4e4d-97f2-78099bfac628"
        const val API_KEY4 = "8a1f756a-6dff-45ed-8336-d980a51e7731"
        const val API_KEY5 = "cb10be9f-4cdb-4020-a46a-3a04b34d829f"
        private val myInterceptor = ApiKeyInterceptor()
        fun createRetrofit():RetrofitAPI {
            return Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor(myInterceptor).build())
                .baseUrl("https://kinopoiskapiunofficial.tech")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI::class.java)

/*            return Retrofit
                .Builder()
                .client(OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }).build())
                .baseUrl("https://kinopoiskapiunofficial.tech")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI::class.java)*/
        }
    }
}