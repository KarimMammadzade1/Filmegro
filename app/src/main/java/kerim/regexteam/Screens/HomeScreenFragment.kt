package kerim.regexteam.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kerim.regexteam.Adapters.FilmItemsAdapter
import kerim.regexteam.Adapters.RecyclerItemClicker

import kerim.regexteam.ViewModel.FViewModel

import kerim.regexteam.Helper.Constants.movieTypess
import kerim.regexteam.Helper.MovieTypes
import kerim.regexteam.Model.ResultFilms
import kerim.regexteam.R
import kotlinx.android.synthetic.main.fragment_home_screen.*

class HomeScreenFragment : Fragment(),AdapterView.OnItemSelectedListener {
    private var filmName:String?=null
    private var filmYear:String?=null
    private var filmType:String?=null
    private lateinit var filmsList:List<ResultFilms>
    private lateinit var filmsAdapter:FilmItemsAdapter
    private lateinit var fViewModel: FViewModel
    private var totalPages:Int=0
    private var currentPage=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fViewModel = ViewModelProvider(this.requireActivity())[FViewModel::class.java]
    }

    override fun onCreateView( inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        filmsAdapter=FilmItemsAdapter(requireContext())
        filmsList= emptyList()
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFilmButton.setOnClickListener {
            filmName = filmTitleEditText.text.toString()
            filmYear = filmYearEditText.text.toString()
            if (filmName.isNullOrBlank() && filmName.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "You Must Enter Film Name!", Toast.LENGTH_LONG).show()
            } else {
                filmTitleEditText.text.clear()
                filmYearEditText.text.clear()
                fViewModel.getTotalFilmInfo(filmName = filmName!!,filmYear = filmYear!!,filmType = filmType!!,filmPage ="1",requireContext())
                progresBarr.visibility = View.VISIBLE
                observeDataForResponse()
                requireActivity().hideSoftKeyboard()
            }
        }
        movieTypeSpinnerConfig()
        filmItemsRecyclerView.addOnItemTouchListener(RecyclerItemClicker(requireContext(),filmItemsRecyclerView,object : RecyclerItemClicker.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        fViewModel.getFilmDetails(filmsList[position].imdbID, requireContext())
                        findNavController().navigate(R.id.fromHomeToDetails)
                        //Toast.makeText(requireContext(), filmsList[position].imdbID, Toast.LENGTH_SHORT).show()

                }

                    override fun onItemLongClick(view: View?, position: Int) {

                    }

                }))
        filmTitleEditText.setOnKeyListener(object:View.OnKeyListener{
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (event?.action == KeyEvent.ACTION_DOWN &&keyCode == KeyEvent.KEYCODE_ENTER) {
                        searchFilmButton.performClick()
                        requireActivity().hideSoftKeyboard()
                    return true
                    }
                    return false
                }


            })

        nextPageButton.setOnClickListener {
            if (currentPage==totalPages){
                nextPageButton.isClickable=false
                Toast.makeText(requireContext(), "Impossible to perform!", Toast.LENGTH_LONG).show()
            }else{
            currentPage += 1
                fViewModel.getTotalFilmInfo(filmName = filmName!!,filmYear = filmYear!!,filmType = filmType!!,filmPage =currentPage.toString(),requireContext())
                observeDataForResponse()
            pageIndicator.text= "Page $currentPage"

        }}
        previousPageButton.setOnClickListener {
            if (currentPage==1){
                previousPageButton.isClickable=false
                Toast.makeText(requireContext(), "Impossible to perform!", Toast.LENGTH_LONG).show()
            }else{
            currentPage -= 1
                fViewModel.getTotalFilmInfo(filmName = filmName!!,filmYear = filmYear!!,filmType = filmType!!,filmPage =currentPage.toString(),requireContext())
                observeDataForResponse()
                pageIndicator.text="Page $currentPage"
        }}

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
             0 -> filmType=MovieTypes.MOVIE.name
             1 -> filmType=MovieTypes.EPISODE.name
             2 -> filmType=MovieTypes.SERIES.name
        }

//        Toast.makeText(requireContext(), "$position", Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(requireContext(), "Nothing", Toast.LENGTH_LONG).show()
    }

    private fun observeDataForResponse() {
        fViewModel.totalFilmInfo.observe(viewLifecycleOwner,{
            if (it==null){
                Toast.makeText(requireContext(), "No Such Movie", Toast.LENGTH_LONG).show()
                progresBarr.visibility=View.GONE
            } else{
                filmsList=it
            setRecyclerViewItems(filmsList)
                setPageData()


            }
        })

    }

    private fun setPageData() {
        previousPageButton.visibility=View.VISIBLE
        nextPageButton.visibility=View.VISIBLE
        pageIndicator.visibility=View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerViewItems(filmList: List<ResultFilms>) {

        filmItemsRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        filmItemsRecyclerView.adapter=filmsAdapter
        filmItemsRecyclerView.visibility=View.VISIBLE
        filmsAdapter.setItemList(filmList.toMutableList())
        totalResultsTextView.visibility=View.VISIBLE.also {
            fViewModel.totalFilmsCount.observe(viewLifecycleOwner,{
                if (it==null){
                    Toast.makeText(requireContext(), "No Such Movie", Toast.LENGTH_LONG).show()
                }else{
                totalResultsTextView.text="Total Films : $it "
                totalPages=(it.toInt()/10)+1
                if (totalPages*10==it.toInt()){
                    totalPages-1
                }
//                Toast.makeText(requireContext(), "$totalPages", Toast.LENGTH_SHORT).show()
                } })
        }
        progresBarr.visibility=View.GONE

    }

    private fun movieTypeSpinnerConfig(){
        filmTypeSpinner.onItemSelectedListener=this
        val spinnerAdapter:ArrayAdapter<*> =ArrayAdapter<MovieTypes?>(requireContext(),R.layout.support_simple_spinner_dropdown_item,movieTypess)
        filmTypeSpinner.setSelection(0)
        filmTypeSpinner.adapter=spinnerAdapter
    }

    override fun onResume() {
        super.onResume()
            observeDataForResponse()
            setRecyclerViewItems(filmsList)
        pageIndicator.text="Page $currentPage"

    }
    private fun Activity.hideSoftKeyboard(){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


}