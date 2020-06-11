package com.sf.gtdng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sf.gtdng.R
import com.sf.gtdng.entity.GithubUserField
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.item_github_user.view.*

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 11/06/20
 */

class FavoriteListAdapter(var context: Context) :
    RecyclerView.Adapter<FavoriteListAdapter.ItemViewHolder>() {

    var items = ArrayList<GithubUserField>()
    var onItemClickListener: (item: GithubUserField, position: Int) -> Unit = { _, _ -> }
    var onUnFavButtonClicked: (item: GithubUserField, position: Int) -> Unit = { _, _ -> }

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

    fun addAll(data: List<GithubUserField>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var item: GithubUserField

        fun bind(position: Int) {
            item = items[position]

//            val imageResource = context.resources.getIdentifier(item.avatar, null, context.packageName)
//            itemView.imageUser.setImageDrawable(context.getDrawable(imageResource))
            itemView.textUser.text = item.fullName
            itemView.textCompany.text = item.company
            itemView.textFollower.text = (item.follower).toString()
            itemView.textRepository.text = (item.repository).toString()
            itemView.buttonFav.isChecked = item.isFavorite == 1
            itemView.buttonFav.setOnClickListener {
                items.remove(item)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                onUnFavButtonClicked(item, position)
            }

            itemView.cvGithubItemUser.setOnClickListener {
                onItemClickListener(item, adapterPosition)
            }
        }
    }
}
