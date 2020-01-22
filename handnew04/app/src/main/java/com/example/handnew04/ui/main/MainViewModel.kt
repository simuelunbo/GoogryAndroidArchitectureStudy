package com.example.handnew04.ui.main

import android.util.Log
import androidx.databinding.ObservableField
import com.example.handnew04.data.MovieData
import com.example.handnew04.data.MovieRepository
import com.example.handnew04.network.NetworkManager

class MainViewModel(val networkManager: NetworkManager) {
    var movieData: ObservableField<List<MovieData>> = ObservableField()
    var isLoading: ObservableField<Boolean> = ObservableField()
    var isNetworkRunning: ObservableField<Boolean> = ObservableField()
    var isInputTextLengthZero: ObservableField<Boolean> = ObservableField()
    var isEmptyResult: ObservableField<Boolean> = ObservableField()
    var failMessage: ObservableField<String> = ObservableField()

    fun serchMovie(inputText: String) {
        isLoading.set(true)

        if (!isConnectedNetwork()) {
            isNetworkRunning.set(false)
            isLoading.set(false)
            return
        }

        if (!checkInputQuery(inputText)) return

        MovieRepository.getMovieData(inputText
            , success = {
                if (it.items.isEmpty()) isEmptyResult.set(true)
                else movieData.set(it.items)
            },
            fail = {
                Log.e("NaverMovieApi Fail ", it.message)
                failMessage.set(it.message)
            })

        isLoading.set(false)
    }

    fun isConnectedNetwork(): Boolean = networkManager.checkNetworkConnect()

    fun checkInputQuery(inputText: String): Boolean {
        return if (inputText.isEmpty()) {
            isInputTextLengthZero.set(true)
            isLoading.set(false)
            false
        } else true
    }
}