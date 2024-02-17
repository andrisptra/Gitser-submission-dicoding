package com.example.gitser.ui.followFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitser.data.response.ItemsItem
import com.example.gitser.databinding.FragmentFollowerBinding
import com.example.gitser.ui.main.UserAdapter
import com.example.gitser.ui.userDetail.UserDetailActivity


class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        val viewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[FollowerViewModel::class.java]
        viewModel.listUser.observe(viewLifecycleOwner) {
            setFollow(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        arguments?.let {
            val position = it.getInt(ARG_SECTION_NUMBER)
            val username = it.getString(ARG_USERNAME).toString()
            if (position == 1) {
                viewModel.getFollowerList(username)
            } else {
                viewModel.getFollowingList(username)
            }
        }
    }

    private fun setFollow(followList: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(followList)
        binding.rvUserList.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(requireActivity(), UserDetailActivity::class.java)
                intentToDetail.putExtra(UserDetailActivity.USERNAME, data.login)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}