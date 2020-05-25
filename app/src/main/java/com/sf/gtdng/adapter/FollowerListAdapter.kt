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
import com.sf.gtdng.DetailUserResultActivity
import com.sf.gtdng.MainActivity
import com.sf.gtdng.R
import com.sf.gtdng.network.response.FollowerListModelItem
import com.sf.gtdng.utils.Extra
import kotlinx.android.synthetic.main.item_github_user.view.*

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 25/05/20
 */


class FollowerListAdapter(var context: Context) :
    RecyclerView.Adapter<FollowerListAdapter.ItemViewHolder>() {

    private var items = ArrayList<FollowerListModelItem>()

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

    fun addAll(data: List<FollowerListModelItem>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var item: FollowerListModelItem

        fun bind(position: Int) {
            item = items[position]
            val activity = (context as DetailUserResultActivity)

            Glide.with(activity).load(item.avatarUrl).into(itemView.imageUser)
            itemView.textUser.text = item.login
        }
    }
}
