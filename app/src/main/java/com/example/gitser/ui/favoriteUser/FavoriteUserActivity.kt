package com.example.gitser.ui.favoriteUser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitser.data.database.FavUserDatabase
import com.example.gitser.data.database.FavUserEntity
import com.example.gitser.data.response.ItemsItem
import com.example.gitser.databinding.ActivityFavoriteUserBinding
import com.example.gitser.helper.ViewModelFactory
import com.example.gitser.ui.userDetail.UserDetailActivity

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private val viewModel by viewModels<FavUserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecycleList()

        val materialToolBar = binding.materialToolbar
        materialToolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.getAllFavUser().observe(this) { users ->
            if (users.isEmpty()){
                binding.userFavNotFound.visibility = View.VISIBLE
            }else{
                getFavUserList(users)
            }

        }
    }

    private fun getFavUserList(users: List<FavUserEntity>) {
        val items = arrayListOf<ItemsItem>()
        users.map {
            val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!)
            items.add(item)
        }
        setFavUserList(items)
    }

    private fun setFavUserList(userList: List<ItemsItem>) {
        val adapter = FavoriteUserAdapter()
        adapter.submitList(userList)
        binding.itemUserList.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
                val intentToDetail =
                    Intent(this@FavoriteUserActivity, UserDetailActivity::class.java)
                intentToDetail.putExtra(UserDetailActivity.USERNAME, data.login)
                intentToDetail.putExtra(UserDetailActivity.AVATARURL, data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showDetailUser(user: ItemsItem) {
        Toast.makeText(this, "You choose " + user.login, Toast.LENGTH_SHORT).show()
    }

    private fun showRecycleList() {
        val layoutManager = LinearLayoutManager(this)
        binding.itemUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.itemUserList.addItemDecoration(itemDecoration)
    }
}