package kerim.regexteam.Screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kerim.regexteam.Adapters.RatingAdapter
import kerim.regexteam.FLocalDataBase.FRoomItemEntity
import kerim.regexteam.ViewModel.FViewModel
import kerim.regexteam.Model.FilmDetailsModel
import kerim.regexteam.Model.Rating
import kerim.regexteam.R
import kotlinx.android.synthetic.main.fragment_film_details.*

class FilmDetailsFragment : Fragment() {
    private lateinit var fViewModel: FViewModel
    private var filmDetails: FilmDetailsModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fViewModel = ViewModelProvider(this.requireActivity())[FViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_film_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmDetailPoster.setOnClickListener {
            findNavController().navigate(R.id.fromDetailsToHome)
        }
        fViewModel.filmDetails.observe(viewLifecycleOwner, {
            filmDetails = it

            setFilmDetails(filmDetails!!)
        })
        fViewModel.userRates.observe(viewLifecycleOwner,{
            val userRateList= mutableListOf<Rating>()
                it?.forEach {fRoomItem ->
                    val newRating=Rating(fRoomItem.ratedUserName, fRoomItem.userRate.toString())
                        userRateList.add(newRating)
                }

            setRatingRecyclerItems(userRateList)

            
            
        })
        addRatingButton.setOnClickListener {
            if (userNameEditText.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Add your name!", Toast.LENGTH_LONG).show()
            } else{
            val userName = userNameEditText.text.toString()
            userNameEditText.text.clear()
            val userRate = ratingBar.rating
             val newRate=FRoomItemEntity(rateId = 0,filmDetails!!.imdbID,userName, userRate )
             fViewModel.addRateToDatabase(newRate)
        }}

    }

    private fun setFilmDetails(filmDetails: FilmDetailsModel) {
        Glide.with(requireContext()).load(filmDetails.Poster).placeholder(R.drawable.loadingpng).into(filmDetailPoster)
        filmDetailName.text = filmDetails.Title
        filmDetailYear.text = filmDetails.Year
        filmDetailRated.text = filmDetails.Rated
        filmDetailRunTime.text = filmDetails.Runtime
        filmDetailLanguage.text = filmDetails.Language
        filmDetailCountry.text = filmDetails.Country
        filmDetailGenre.text = filmDetails.Genre
        filmDetailDirector.text = filmDetails.Director
        filmDetailWriters.text = filmDetails.Writer
        filmDetailBoxOffice.text = filmDetails.BoxOffice
        filmDetailImdbRating.text = filmDetails.imdbRating
        filmDetailMetaScore.text = filmDetails.Metascore
        filmDetailActors.text = filmDetails.Actors
        filmDetailPlot.text = filmDetails.Plot
        filmDetailAwards.text = filmDetails.Awards
        setRatingRecyclerItems()

    }

    private fun setRatingRecyclerItems( userRatings: List<Rating>?=null ) {
        fViewModel.getRatesFromDatabase(filmDetails!!.imdbID)
        var ratingList=filmDetails!!.Ratings.toMutableList()
        val adapter = RatingAdapter()
        filmRatingsRecyclerView.adapter = adapter

        filmRatingsRecyclerView.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if(userRatings!=null){
            ratingList.addAll(userRatings)

        }
        adapter.setRatingItemList(ratingList)

    }

}