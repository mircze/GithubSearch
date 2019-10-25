package com.mircze.githubsearch.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mircze.githubsearch.R
import com.mircze.githubsearch.model.GithubRepo
import com.mircze.githubsearch.networking.ApiCallback
import com.mircze.githubsearch.networking.ApiManager

class MainActivity : AppCompatActivity(), ListItemListener<GithubRepo> {

    companion object {
        private const val KEY_RESULTS = "KEY_RESULTS"
    }

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var mainList: RecyclerView
    private lateinit var loader: ProgressBar
    private lateinit var errorMessage: TextView
    private var currentResults: MutableList<GithubRepo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchInput = findViewById(R.id.activity_main_search_input)
        searchButton = findViewById(R.id.activity_main_search_button)
        mainList = findViewById(R.id.activity_main_main_list)
        loader = findViewById(R.id.activity_main_loader)
        errorMessage = findViewById(R.id.activity_main_error_message)
        mainList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getDataFromBackend(searchInput.text.toString())
                true
            } else {
                false
            }
        }
        searchButton.setOnClickListener {
            getDataFromBackend(searchInput.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_RESULTS, ArrayList(currentResults))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedResults = savedInstanceState.getParcelableArrayList<GithubRepo>(KEY_RESULTS)
        if (savedResults?.isNotEmpty() == true) {
            currentResults.clear()
            currentResults.addAll(savedResults)
            initializeData(currentResults.toList())
        }
    }

    private fun getDataFromBackend(query: String) {
        hideSoftKeyboard()
        if (query.isNotEmpty()) {
            errorMessage.visibility = View.GONE
            loader.visibility = View.VISIBLE
            ApiManager.instance.getRepositoriesByQuery(
                query,
                object : ApiCallback<List<GithubRepo>> {
                    override fun onSuccess(result: List<GithubRepo>) {
                        initializeData(result)
                        loader.visibility = View.GONE
                    }

                    override fun onError(exception: Throwable?) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error fetching data. Reason: ${exception?.message ?: "Unknown"}",
                            Toast.LENGTH_LONG
                        ).show()
                        loader.visibility = View.GONE
                        showErrorEmptyQuery()
                    }
                })
        } else {
            showErrorEmptyQuery()
        }
    }

    private fun initializeData(data: List<GithubRepo>) {
        currentResults.clear()
        currentResults.addAll(data)
        val adapter = GithubRepoAdapter(data, this)
        mainList.adapter = adapter
        if (currentResults.isEmpty()) {
            showErrorEmptyResults()
        } else {
            errorMessage.visibility = View.GONE
        }
    }

    private fun showErrorEmptyQuery() {
        currentResults.clear()
        mainList.adapter = null
        errorMessage.setText(R.string.message_empty_search)
        errorMessage.visibility = View.VISIBLE
    }

    private fun showErrorEmptyResults() {
        errorMessage.setText(R.string.message_empty_results)
        errorMessage.visibility = View.VISIBLE
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Open browser error. Reason: ${e.message ?: "Unknown"}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun hideSoftKeyboard() {
        currentFocus?.let {
            with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
                hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    //region ListItemListener
    override fun onItemClicked(item: GithubRepo) {
        if (item.htmlUrl?.isNotEmpty() == true) {
            openBrowser(item.htmlUrl)
        }
    }
    //endregion
}
