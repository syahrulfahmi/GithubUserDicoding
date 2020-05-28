package com.sf.gtdng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sf.gtdng.DetailUserResultActivity
import com.sf.gtdng.R
import com.sf.gtdng.network.response.FollowingAndFollowerListItem
import com.sf.gtdng.utils.loadUrl
import kotlinx.android.synthetic.main.item_github_user.view.*

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 25/05/20
 */


class FollowerListAdapter(var context: Context) :
    RecyclerView.Adapter<FollowerListAdapter.ItemViewHolder>() {

    var items = ArrayList<FollowingAndFollowerListItem>()

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

    fun addAll(data: List<FollowingAndFollowerListItem>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var item: FollowingAndFollowerListItem

        fun bind(position: Int) {
            item = items[position]

            itemView.imageUser.loadUrl(item.avatarUrl)
            itemView.textUser.text = item.login
        }
    }
}
