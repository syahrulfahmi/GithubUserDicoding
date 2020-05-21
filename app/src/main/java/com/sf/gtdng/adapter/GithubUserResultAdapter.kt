package com.sf.gtdng.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sf.gtdng.DetailActivity
import com.sf.gtdng.MainActivity
import com.sf.gtdng.R
import com.sf.gtdng.model.User
import com.sf.gtdng.network.response.ItemUserGithub
import com.sf.gtdng.utils.Extra
import kotlinx.android.synthetic.main.item_github_user.view.*


class GithubUserResultAdapter(var context: Context) :
    RecyclerView.Adapter<GithubUserResultAdapter.ItemViewHolder>() {

    var items = ArrayList<ItemUserGithub>()
    var onItemClickListener: (data: ItemUserGithub, position: Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_github_user, parent, false)
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
            val activity = (context as MainActivity)

            Glide.with(activity).load(item.avatarUrl).into(itemView.imageUser)
            itemView.textUser.text = item.login

            itemView.cvGithubItemUser.setOnClickListener {
                onItemClickListener(item, adapterPosition)
            }
        }
    }
}
