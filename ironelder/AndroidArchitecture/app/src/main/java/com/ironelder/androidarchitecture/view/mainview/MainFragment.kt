package com.ironelder.androidarchitecture.view.mainview

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ironelder.androidarchitecture.R
import com.ironelder.androidarchitecture.common.BLOG
import com.ironelder.androidarchitecture.common.TYPE_KEY
import com.ironelder.androidarchitecture.component.CustomListViewAdapter
import com.ironelder.androidarchitecture.data.ResultItem
import com.ironelder.androidarchitecture.data.database.SearchResultDatabase
import com.ironelder.androidarchitecture.view.baseview.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_search_listview.*


class MainFragment :
    BaseFragment<MainContract.View, MainContract.Presenter>(R.layout.fragment_main),
    MainContract.View {

    override val presenter = MainPresenter()

    private val mType: String? by lazy {
        arguments?.getString(TYPE_KEY)
    }

    private var mSearchWord: String? = null

    override fun showNoSearchData() {
        (rv_resultListView?.adapter as? CustomListViewAdapter)?.clearItemList()
        customInfoView.noSearchDate()
    }

    override fun onDataChanged(result: ArrayList<ResultItem>) {
        (rv_resultListView?.adapter as? CustomListViewAdapter)?.setItemList(result)
        presenter.setSearchResultToRoom(
            SearchResultDatabase.getInstance(
                context?.applicationContext ?: (this.activity as Context).applicationContext
            )?.SearchResultDao(), mType ?: BLOG, mSearchWord ?: "", result
        )
    }

    override fun showErrorMessage(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            .show()
    }

    override fun showLoading() {
        customInfoView.startLoading()
    }

    override fun hideLoading() {
        customInfoView.stopLoading()
    }

    override fun onLoadFromDatabase(searchWord: String, result: ArrayList<ResultItem>) {
        mSearchWord = searchWord
        (rv_resultListView?.adapter as? CustomListViewAdapter)?.setItemList(result)
    }

    override fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        with(rv_resultListView) {
            adapter =
                CustomListViewAdapter(mType ?: BLOG)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager(context).orientation
                )
            )
        }
    }

    override fun doLoadFromDatabase() {
        presenter.getSearchResultToRoom(
            context?.applicationContext ?: (this.activity as Context).applicationContext,
            mType ?: BLOG
        )
    }

    override fun doCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView =
            SearchView((context as? MainActivity)?.supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search)?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mSearchWord = query ?: ""
                searchView.clearFocus()
                presenter.searchWithAdapter(
                    mType ?: BLOG,
                    query
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnSearchClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!mSearchWord.isNullOrEmpty()) {
                    searchView.setQuery(mSearchWord, false)
                }
            }

        })
    }

    companion object {
        fun newInstance(type: String?): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_KEY, type)
                }
            }
        }
    }

}