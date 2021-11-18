package kerim.regexteam.ViewModel

import android.app.Application

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kerim.regexteam.FLocalDataBase.FRoomDb
import kerim.regexteam.FLocalDataBase.FRoomItemEntity
import kerim.regexteam.Model.FilmDetailsModel
import kerim.regexteam.Model.ResultFilms
import kerim.regexteam.Model.SearchFilmModel
import kerim.regexteam.Network.NetworkService
import kerim.regexteam.Network.ServiceBuilder.retrofitOmdb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FViewModel(application: Application) : AndroidViewModel(application) {
    private var searchResultData:SearchFilmModel?=null
    private var filmDetailsResultData:FilmDetailsModel?=null
    private val filmDataDao = FRoomDb.getDatabase(application).filmDataDao()
    private val retroService=retrofitOmdb.create(NetworkService::class.java)


    private val _totalFilmInfo = MutableLiveData<List<ResultFilms>>()
    val totalFilmInfo: LiveData<List<ResultFilms>> = _totalFilmInfo
    private val _totalFilmsCount = MutableLiveData<String>()
    val totalFilmsCount: LiveData<String> = _totalFilmsCount

    fun getTotalFilmInfo(filmName: String="", filmYear: String="", filmType:String="",filmPage:String="1", context: Context) {

            val call = retroService.getFilmData(filmTitle = filmName,releaseYear = filmYear,type = filmType,page = filmPage)
            call.enqueue(object: Callback<SearchFilmModel>{
                override fun onResponse(call: Call<SearchFilmModel>,response: Response<SearchFilmModel>) {
        if (response.isSuccessful&&response.body()!=null){
            if (response.body()!!.Response=="False"){
                Toast.makeText(context, "Movie Not Found!", Toast.LENGTH_SHORT).show()
                             } else {

                    searchResultData=response.body()
                  _totalFilmInfo.value= searchResultData!!.Search
                    _totalFilmsCount.value=searchResultData!!.totalResults

            }
}

                }

                override fun onFailure(call: Call<SearchFilmModel>, t: Throwable) {

                }

            })


    }

    private val _filmDetails=MutableLiveData<FilmDetailsModel>()
    val filmDetails:LiveData<FilmDetailsModel> = _filmDetails

    fun getFilmDetails(imdbID: String,context: Context) {
      val call=retroService.getFilmDetails(filmid = imdbID)
      call.enqueue(object:Callback<FilmDetailsModel>{
          override fun onResponse(call: Call<FilmDetailsModel>,response: Response<FilmDetailsModel>) {
              if (response.isSuccessful && response.body() != null) {
                  if (response.body()!!.Response == "False") {
                      Toast.makeText(context, "Movie ID Not Found!", Toast.LENGTH_SHORT).show()
                  }
                  filmDetailsResultData = response.body()
                  _filmDetails.value=filmDetailsResultData!!


              }
          }

          override fun onFailure(call: Call<FilmDetailsModel>, t: Throwable) {
              Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
          }

      })
    }

    fun addRateToDatabase(newRate:FRoomItemEntity){
        viewModelScope.launch(Dispatchers.IO) {
            filmDataDao.insertData(newRate)
        }
    }

    private val _userRates=MutableLiveData<List<FRoomItemEntity>?>()
    val userRates:LiveData<List<FRoomItemEntity>?> = _userRates
    fun getRatesFromDatabase(filmId:String) {
        viewModelScope.launch(Dispatchers.IO){
            val userRatings=filmDataDao.getData(filmId)
            withContext(Dispatchers.Main){
            _userRates.value=userRatings
            }
        }
    }

}