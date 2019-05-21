package com.suresh.movieslistpopular.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.suresh.movieslistpopular.R
import com.suresh.movieslistpopular.activities.MovieDescriptionActivity
import com.suresh.movieslistpopular.api.APIConstants
import com.suresh.movieslistpopular.data.MoviesListData
import com.suresh.movieslistpopular.db.AppDatabase
import com.suresh.movieslistpopular.utils.Constants
import kotlinx.android.synthetic.main.item_movies_list.view.*

class MovieListShowAdapter(private val context: Context, private val moviesList: MutableList<MoviesListData.Result>) : RecyclerView.Adapter<MovieListShowAdapter.MovieListHolder>() {

    private lateinit var db: AppDatabase

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MovieListHolder {
        return MovieListHolder(LayoutInflater.from(context).inflate(R.layout.item_movies_list, parent, false))
    }

    override fun getItemCount(): Int {

        return moviesList.size
    }

    override fun onBindViewHolder(holder: MovieListHolder, position: Int) {

        holder.bindview(position)
    }


    inner class MovieListHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        @SuppressLint("SetTextI18n")
        internal fun bindview(position: Int) {
            val bean = moviesList[position]

            itemView.tvTitle.text = bean.title
            itemView.tvPopularity.text = bean.popularity.toString()
            itemView.tvReleaseDate.text = bean.release_date
            itemView.tvAdult.text = bean.adult.toString()
            itemView.tvRating.text = bean.vote_average.toString() + "" + "/10"
            itemView.tvDescription.text = bean.overview
            Glide.with(context).load(APIConstants.IMAGE_URL + bean.poster_path).into(itemView.ivProfile)

        }

        init {
            db = AppDatabase.getInstance(context)!!
            itemView.layContainer.setOnClickListener(this)
            itemView.ivDel.setOnClickListener(this)


        }

        override fun onClick(v: View?) {

            when (v!!.id) {
                R.id.layContainer -> {
                    val intent = Intent(context, MovieDescriptionActivity::class.java)
                    intent.putExtra(Constants.movieData, moviesList[adapterPosition])
                    context.startActivity(intent)
                }

                R.id.ivDel -> {


                    val localListSize: Int = db.contactDAO.contacts.size

                    if (localListSize != 0 && adapterPosition < localListSize)
                        db.contactDAO.delete(moviesList[adapterPosition])

                    moviesList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)

                }

            }

        }


    }


}