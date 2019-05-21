package com.suresh.movieslistpopular.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.suresh.movieslistpopular.R
import com.suresh.movieslistpopular.adapter.MovieListShowAdapter
import com.suresh.movieslistpopular.api.WebServiceRequests
import com.suresh.movieslistpopular.data.MoviesListData
import com.suresh.movieslistpopular.db.AppDatabase
import com.suresh.movieslistpopular.utils.Constants

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var moviesList: MutableList<MoviesListData.Result>
    private var layoutManager: LinearLayoutManager? = null
    private var movieListShowAdapter: MovieListShowAdapter? = null
    private lateinit var db: AppDatabase

    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pastVisiblesItems: Int = 0
    private var pgno = 1
    private var isCalled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
        listener()
        setData()


    }

    private fun init() {

        moviesList = ArrayList()
        db = AppDatabase.getInstance(this)!!

        layoutManager = LinearLayoutManager(this)
        rvMoviesList.layoutManager = layoutManager

        movieListShowAdapter = MovieListShowAdapter(this@HomeActivity, moviesList)
        rvMoviesList.adapter = movieListShowAdapter

        progressbarTop.visibility = View.VISIBLE

    }


    private fun setData() {

        moviesList.addAll(db.contactDAO.contacts)
        val localMovieListSize: Int = moviesList.size

        if (localMovieListSize > 0) {
            pgno = moviesList[localMovieListSize - 1].page + 1
            movieListShowAdapter!!.notifyDataSetChanged()
            progressVisibility()

        } else {
            if (Constants.isNetConnected(this))
                getMovieList(pgno)
            else noNetConnection()
        }
    }

    private fun listener() {

        rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager!!.childCount
                totalItemCount = layoutManager!!.itemCount
                pastVisiblesItems = layoutManager!!.findFirstVisibleItemPosition()

                if (dy > 0)
                //check for scroll down
                {
                    visibleItemCount = layoutManager!!.childCount
                    totalItemCount = layoutManager!!.itemCount
                    pastVisiblesItems = layoutManager!!.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        //Do pagination.. i.e. fetch new data
                        if (pgno != -1 && !isCalled) {
                            isCalled = true
                            progressBar.visibility = View.VISIBLE
                            if (Constants.isNetConnected(this@HomeActivity))
                                getMovieList(pgno)
                            else noNetConnection()
                        }
                    }
                }

                if (visibleItemCount == totalItemCount) {
                    if (pgno != -1 && !isCalled) {
                        isCalled = true
                        progressBar.visibility = View.VISIBLE
                        if (Constants.isNetConnected(this@HomeActivity))
                            getMovieList(pgno)
                        else noNetConnection()
                    }
                }


            }
        })

    }


    private fun getMovieList(pgno1: Int) {

        WebServiceRequests.getInstance().getPopularMoviesList(pgno1, object : Callback<MoviesListData> {
            override fun onResponse(call: Call<MoviesListData>?, response: Response<MoviesListData>?) {
                progressVisibility()

                if (response!!.isSuccessful)

                    if (pastVisiblesItems < layoutManager!!.itemCount)
                        layoutManager!!.scrollToPosition(pastVisiblesItems)

                pgno = response.body()!!.page + 1
                isCalled = false

                if (response.body()!!.page == response.body()!!.total_pages) {
                    pgno = -1
                }
                moviesList.addAll(response.body()!!.results)
                movieListShowAdapter!!.notifyDataSetChanged()



                response.body()!!.results.forEach {
                    it.page = response.body()!!.page
                    it.total_pages = response.body()!!.total_pages
                    db.contactDAO.insert(it)
                }


            }

            override fun onFailure(call: Call<MoviesListData>?, t: Throwable?) {
                progressVisibility()

            }
        })

    }

    private fun progressVisibility() {
        progressbarTop.visibility = View.GONE
        progressBar.visibility = View.GONE

    }

    private fun noNetConnection() {
        Constants.toast(this@HomeActivity, getString(R.string.no_internet_connection))
        progressVisibility()
    }

}

