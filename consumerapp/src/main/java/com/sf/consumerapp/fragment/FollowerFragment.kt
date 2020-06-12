package com.sf.consumerapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sf.consumerapp.DetailUserActivity
import com.sf.consumerapp.R
import com.sf.consumerapp.adapter.FollowerListAdapter
import com.sf.consumerapp.helper.Extra
import com.sf.consumerapp.viewModel.GithubUserViewModel
import kotlinx.android.synthetic.main.fragment_list_follower.*
import kotlinx.android.synthetic.main.fragment_list_follower.progressLoading
import kotlinx.android.synthetic.main.fragment_list_follower.textInfo

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 25/05/20
 */

class FollowerFragment : Fragment() {

    private lateinit var followerListAdapter: FollowerListAdapter
    private lateinit var viewModel: GithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.run {
            viewModel = ViewModelProvider(
                activity!!,
                ViewModelProvider.NewInstanceFactory()
            ).get(GithubUserViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_list_follower, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followerListAdapter = FollowerListAdapter(context!!)

        viewModel.getUserFollower().observe(viewLifecycleOwner, Observer {
            rvFollowerList.adapter = followerListAdapter
            followerListAdapter.addAll(it)
            progressLoading.visibility = View.GONE
            if (followerListAdapter.items.isEmpty()) {
                rvFollowerList.visibility = View.GONE
                textInfo.visibility = View.VISIBLE
            } else {
                rvFollowerList.visibility = View.VISIBLE
                textInfo.visibility = View.GONE
            }
        })
        initListener()
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