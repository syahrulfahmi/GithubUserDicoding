package com.sf.gtdng.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.sf.gtdng.DetailActivity
import com.sf.gtdng.MainActivity
import com.sf.gtdng.R
import com.sf.gtdng.model.User
import com.sf.gtdng.utils.Extra
import kotlinx.android.synthetic.main.item_github_user.view.*


class GithubUserAdapter(var context: Context) :
    RecyclerView.Adapter<GithubUserAdapter.ItemViewHolder>() {

    private var items = ArrayList<User>()

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

    fun addAll(data: List<User>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var item: User

        fun bind(position: Int) {
            item = items[position]
            val activity = (context as MainActivity)

            val imageResource = context.resources.getIdentifier(item.avatar, null, context.packageName)
            itemView.imageUser.setImageDrawable(context.getDrawable(imageResource))
            itemView.textUser.text = item.name
            itemView.textCompany.text = item.company
            itemView.textFollower.text = (item.follower).toString()
            itemView.textRepository.text = (item.repository).toString()

            itemView.cvGithubItemUser.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java).apply {
                    putExtra(Extra.DATA, item)
                }
                activity.startActivity(intent)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity,
//                        itemView.imageUser,
//                        Extra.IMAGE
//                    )
//                    activity.startActivity(intent, options.toBundle())
//                } else {
//                }
            }
        }
    }
}
