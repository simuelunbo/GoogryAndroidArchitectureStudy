package com.buddman1208.architecturestudy

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.buddman1208.architecturestudy.models.BookItem
import com.buddman1208.architecturestudy.models.CommonItem
import com.buddman1208.architecturestudy.models.MovieItem
import com.buddman1208.architecturestudy.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val datas: DataSource<Any> = dataSourceTypedOf()
    var currentMode: String = Constants.MODE_BLOG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initToolbar()
        initBottomNavigation()
    }

    private fun initToolbar() {
        toolbar.apply {
            setSupportActionBar(this)
            setTitleTextColor(ContextCompat.getColor(applicationContext, android.R.color.white))
            setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            contentInsetStartWithNavigation = 0
        }
        setSearchType(bottomTabView.selectedItemId)
    }

    private fun initBottomNavigation() {
        bottomTabView.setOnNavigationItemSelectedListener {
            setSearchType(it.itemId)
            true
        }
    }

    private fun initRecyclerView() {
        rvMain.setup {
            withDataSource(datas)
            withLayoutManager(LinearLayoutManager(this@MainActivity))
            withItem<CommonItem, CommonViewHolder>(R.layout.content_common_item) {
                onBind(::CommonViewHolder) { index, item ->
                }
            }
            withItem<MovieItem, MovieViewHolder>(R.layout.content_movie_item) {
                onBind(::MovieViewHolder) { index, item ->
                }
            }
            withItem<BookItem, BookViewHolder>(R.layout.content_book_item) {
                onBind(::BookViewHolder) { index, item ->
                }
            }

        }
    }

    private fun setSearchType(@IdRes itemId: Int) {
        currentMode = when (itemId) {
            R.id.menuBlog -> Constants.MODE_BLOG
            R.id.menuBook -> Constants.MODE_BOOK
            R.id.menuMovie -> Constants.MODE_MOVIE
            R.id.menuNews -> Constants.MODE_NEWS
            else -> ""
        }
        updateToolbarTitle()
    }

    private fun updateToolbarTitle() {
        supportActionBar?.title = when (currentMode) {
            Constants.MODE_BLOG -> "블로그"
            Constants.MODE_BOOK -> "도서"
            Constants.MODE_MOVIE -> "영화"
            Constants.MODE_NEWS -> "뉴스"
            else -> ""
        }
    }

}

class CommonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvDescription: TextView = view.findViewById(R.id.tvDescription)
}

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvSubtitleDate: TextView = view.findViewById(R.id.tvSubtitleDate)
    val tvDirectorActor: TextView = view.findViewById(R.id.tvDirectorActor)
}

class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvAuthorPublisherDate: TextView = view.findViewById(R.id.tvAuthorPublisherDate)
    val tvDescription: TextView = view.findViewById(R.id.tvDescription)
}