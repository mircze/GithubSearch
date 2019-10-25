package com.mircze.githubsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mircze.githubsearch.R
import com.mircze.githubsearch.model.GithubRepo

/**
 * Created by Mirosław Juda on 25.10.2019.
 */
class GithubRepoAdapter(private val data: List<GithubRepo>, private val listener: ListItemListener<GithubRepo>? = null) :
    RecyclerView.Adapter<GithubRepoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        with(holder) {
            name.text = item.name ?: "---"
            description.text = item.description ?: "---"
            language.text = item.language ?: "---"
            stars.text = "★ ${item.stars}"
            holder.itemView.setOnClickListener {
                listener?.onItemClicked(item)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_search_name)
        val description: TextView = view.findViewById(R.id.item_search_description)
        val language: TextView = view.findViewById(R.id.item_search_language)
        val stars: TextView = view.findViewById(R.id.item_search_star)
    }
}
