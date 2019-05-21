package com.suresh.movieslistpopular.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.suresh.movieslistpopular.R
import com.suresh.movieslistpopular.api.APIConstants
import com.suresh.movieslistpopular.data.MoviesListData
import com.suresh.movieslistpopular.utils.Constants
import kotlinx.android.synthetic.main.activity_movie_description.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.ExecutionException


class MovieDescriptionActivity : AppCompatActivity() {


    private var movieData: MoviesListData.Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_description)

        val bundle: Bundle = intent.extras
        movieData = bundle.getParcelable(Constants.movieData)
        setData()


    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        tvTitleHeading.text = movieData!!.title + getString(R.string.movie)
        tvTitle.text = movieData!!.title
        tvPopularity.text = movieData!!.popularity.toString()
        tvReleaseDate.text = movieData!!.release_date
        tvAdult.text = movieData!!.adult.toString()
        tvRating.text = movieData!!.vote_average.toString() + getString(R.string.slace_ten)
        tvDescription.text = movieData!!.overview
        Glide.with(this).load(APIConstants.IMAGE_URL + movieData!!.poster_path).into(ivProfile)


        try {
            doAsync {
                val theBitmap: Bitmap = Glide.with(this@MovieDescriptionActivity).asBitmap().load(APIConstants.IMAGE_URL + movieData!!.poster_path).submit().get()
                uiThread {
                    val blurred = Constants.blurRenderScript(this@MovieDescriptionActivity, theBitmap, 25)
                    iv_cover.setImageBitmap(blurred)
                }
            }


        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }


    }
}
