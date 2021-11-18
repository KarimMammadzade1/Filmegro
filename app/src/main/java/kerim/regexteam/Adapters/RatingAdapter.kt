package kerim.regexteam.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kerim.regexteam.Model.Rating
import kerim.regexteam.R
import kotlinx.android.synthetic.main.rating_item_row.view.*

class RatingAdapter(): RecyclerView.Adapter<RatingAdapter.RatingsViewHolder>() {
    private var aRatingList: MutableList<Rating>? = null
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RatingAdapter.RatingsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rating_item_row,parent,false)
        return RatingsViewHolder(view)
    }
    fun setRatingItemList(ratingList:MutableList<Rating>){
        aRatingList?.clear()
        aRatingList=ratingList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RatingAdapter.RatingsViewHolder, position: Int) {
        holder.bindItems(aRatingList!![position])
    }
    inner class RatingsViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ratingSource=view.ratingSource
        val ratingValue=view.ratingValue
        fun bindItems(rating: Rating) {
            ratingSource.text=rating.Source
            ratingValue.text=rating.Value
        }

    }
    override fun getItemCount(): Int=aRatingList!!.size
}