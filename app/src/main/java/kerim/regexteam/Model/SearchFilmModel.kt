package kerim.regexteam.Model

import com.google.gson.annotations.SerializedName

data class SearchFilmModel(
    @SerializedName("Response")
    val Response: String,
    @SerializedName("Search")
    val Search: List<ResultFilms>,
    @SerializedName("totalResults")
    val totalResults: String
)