package kerim.regexteam.Network

import kerim.regexteam.Helper.Constants.API_KEY
import kerim.regexteam.Model.FilmDetailsModel
import kerim.regexteam.Model.SearchFilmModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    //https://www.omdbapi.com/?apikey=f1693b75&s=SPIDER&page=1
    @GET(".")
    fun getFilmData(
        @Query("apikey") key: String = API_KEY,
        @Query("s") filmTitle: String,
        @Query("y") releaseYear: String? = "",
        @Query("type") type: String? = "",
        @Query("page") page: String? = "1",
    ): Call<SearchFilmModel>

    //i=tt1285016&plot=full
    @GET(".")
    fun getFilmDetails(
        @Query("apikey") key: String = API_KEY,
        @Query("i") filmid: String,
    ): Call<FilmDetailsModel>

}