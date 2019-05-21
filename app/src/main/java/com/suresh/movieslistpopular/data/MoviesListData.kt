package com.suresh.movieslistpopular.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.suresh.movieslistpopular.utils.Constants
import kotlinx.android.parcel.Parcelize


data class MoviesListData(
        val page: Int,
        val results: MutableList<Result>,
        val total_pages: Int,
        val total_results: Int
)
{



    @Entity(tableName = Constants.movieData)
    @Parcelize
    data class Result(
            @PrimaryKey(autoGenerate = true) var idPrimary: Long?,
            val adult: Boolean,
            val backdrop_path: String,
            val id: Int,
            val original_language: String,
            val original_title: String,
            val overview: String,
            val popularity: Double,
            val poster_path: String,
            val release_date: String,
            val title: String,
            val video: Boolean,
            val vote_average: Double,
            val vote_count: Int,
            var page:Int,
            var total_pages:Int
    ) :Parcelable {  }
}