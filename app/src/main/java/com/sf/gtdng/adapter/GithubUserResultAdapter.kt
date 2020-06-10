package com.sf.gtdng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sf.gtdng.R
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.helper.loadUrl
import kotlinx.android.synthetic.main.item_github_user_search.view.*

class GithubUserResultAdapter(var context: Context) :
    RecyclerView.Adapter<GithubUserResultAdapter.ItemViewHolder>() {

    var items = ArrayList<ItemUserGithub>()
    var onItemClickListener: (data: ItemUserGithub, position: Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_github_user_search, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        itemViewHolder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addAll(data: List<ItemUserGithub>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    fun removeAll() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var item: ItemUserGithub

        fun bind(position: Int) {
            item = items[position]

            itemView.imageUser.loadUrl(item.avatarUrl)
            itemView.textUser.text = item.login

            itemView.setOnClickListener {
                onItemClickListener(item, adapterPosition)
            }
        }
    }
}
