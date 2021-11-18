package kerim.regexteam.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.capitalize
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kerim.regexteam.Model.ResultFilms
import kerim.regexteam.R
import kotlinx.android.synthetic.main.film_item_row.view.*
import java.util.*

class FilmItemsAdapter(private val context: Context) : RecyclerView.Adapter<FilmItemsAdapter.FilmViewHolder>() {
    private var aFilmsList: MutableList<ResultFilms>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.film_item_row, parent, false)
        return FilmViewHolder(view)
    }

    fun setItemList(filmsList: MutableList<ResultFilms>) {
        aFilmsList?.clear()
        aFilmsList = filmsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bindItems(aFilmsList!![position])
    }

 inner  class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val filmPoster = view.filmPoster
        val filmName = view.filmName
        val filmGenre = view.filmGenre
        val filmYear = view.filmYear
        fun bindItems(filmItem: ResultFilms) {
        Glide.with(context).load(filmItem.Poster).placeholder(R.drawable.loadingpng).into(filmPoster)

            filmGenre.text= filmItem.Type.capitalize()
            filmYear.text=filmItem.Year
            filmName.text=filmItem.Title

        }
    }


    override fun getItemCount(): Int = aFilmsList!!.size

}