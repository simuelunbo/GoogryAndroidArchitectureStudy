package com.lllccww.studyforkproject.view.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lllccww.studyforkproject.R
import com.lllccww.studyforkproject.SearchRetrofit
import com.lllccww.studyforkproject.model.Movie
import com.lllccww.studyforkproject.model.MovieItem
import com.lllccww.studyforkproject.view.adapter.MovieListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    private var start = 1
    private var total = 0
    private var display = 0
    private var movieList = ArrayList<MovieItem>()
    lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rv_movie_list.layoutManager = LinearLayoutManager(this)
        movieListAdapter = MovieListAdapter(this)
        rv_movie_list.adapter = movieListAdapter

        rv_movie_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount -1
                if (lastVisibleItemPosition  == itemTotalCount) { //스크롤 마지막(바닥)
                    Log.d("fail : ", "---------------바닥------")
                    if (start < total) {
                        requestSearchMovie(start + 10)
                    } else {
                        Toast.makeText(this@MainActivity, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })


        btn_search.setOnClickListener {
            movieListAdapter.clear()
            closeKeyboard()
            requestSearchMovie(start)
        }
    }


    //영화정보 요청
    private fun requestSearchMovie(pageStart: Int) {
        SearchRetrofit.getService()
            .listMovie(keyword = edt_search_keyword.text.toString(), start = pageStart)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.d("fail : ", t.message)
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    if (response.isSuccessful) {
                        //movieList.clear()


                        val movieData = response.body()
                        Log.d("datadata : ", movieData!!.display)
                        if (movieData!!.display == "0") {
                            Toast.makeText(this@MainActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Log.d("movie : ", movieData.toString())

                        total = Integer.parseInt(movieData!!.total)
                        start = Integer.parseInt(movieData!!.start)
                        display = Integer.parseInt(movieData!!.display)
                        Log.d("start : ", start.toString())
                        Log.d("total : ", total.toString())
                        Log.d("display : ", display.toString())

                      /*  for (i in 0 until display) {
                            movieList!!.add(movieData.items[i])
                            Log.d("movieList : ", movieList.toString())
                        }*/
                        movieListAdapter.setItems(movieData.items)


                  /*      movieListAdapter = MovieListAdapter(this@MainActivity)
                        rv_movie_list.adapter = movieListAdapter
                        movieListAdapter.notifyDataSetChanged()*/

                    }
                }

            })
    }


    //키보드 숨기기
    fun closeKeyboard() {
        var view = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}
