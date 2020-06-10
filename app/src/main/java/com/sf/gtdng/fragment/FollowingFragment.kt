package com.sf.gtdng.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sf.gtdng.DetailUserActivity
import com.sf.gtdng.R
import com.sf.gtdng.adapter.FollowerListAdapter
import com.sf.gtdng.helper.Extra
import com.sf.gtdng.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.fragment_list_following.*

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 25/05/20
 */

class FollowingFragment : Fragment() {

    private lateinit var followerListAdapter: FollowerListAdapter
    private lateinit var viewModel: GithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.run {
            viewModel = ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory()).get(
                GithubUserViewModel::class.java
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_list_following, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerListAdapter = FollowerListAdapter(context!!)
        initListener()

        viewModel.getUserFollowing().observe(viewLifecycleOwner, Observer {
            rvFollowingList.adapter = followerListAdapter
            followerListAdapter.addAll(it)
            progressLoading.visibility = View.GONE
            if (followerListAdapter.items.isEmpty()) {
                textInfo.visibility = View.VISIBLE
                rvFollowingList.visibility = View.GONE
            } else {
                rvFollowingList.visibility = View.VISIBLE
                textInfo.visibility = View.GONE
            }
        })
    }

    private fun initListener() {
        followerListAdapter.onItemClickedListener = { item, _ ->
            val intent = Intent(activity!!, DetailUserActivity::class.java).apply {
                putExtra(Extra.DATA, item.login)
            }
            startActivity(intent)
        }
    }
}