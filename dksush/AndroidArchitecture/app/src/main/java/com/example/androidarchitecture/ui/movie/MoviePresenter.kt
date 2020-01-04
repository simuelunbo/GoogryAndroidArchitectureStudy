package com.example.androidarchitecture.ui.movie

import android.text.TextUtils
import android.util.Log
import com.example.androidarchitecture.data.repository.NaverRepoInterface
import com.example.androidarchitecture.data.response.MovieData
import com.example.androidarchitecture.ui.base.ItemContract

class MoviePresenter(
    private val view: ItemContract.View<MovieData>,
    private val naverRepositroy: NaverRepoInterface
) :
    ItemContract.Presenter {

    override suspend fun requestSearchHist() {
        naverRepositroy.getMovieHist().let {
            if (it.isNotEmpty()) {
                view.renderItems(it)
                view.inputKeyword(naverRepositroy.getMoiveKeyword())

            }

        }

    }


    override fun requestList(text: String) {
        if (TextUtils.isEmpty(text)) {
            view.blankInputText()
        } else {
            naverRepositroy.saveMovieKeyword(text)
            naverRepositroy.getMovie(text, 1, 10,
                success = {
                    view.renderItems(it)
                    view.goneEmptyText()
                }, fail = {
                    view.errorToast(it.toString())

                })
        }

    }
}