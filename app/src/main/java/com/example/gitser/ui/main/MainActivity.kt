package com.example.gitser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitser.R
import com.example.gitser.data.response.ItemsItem
import com.example.gitser.databinding.ActivityMainBinding
import com.example.gitser.ui.favoriteUser.FavoriteUserActivity
import com.example.gitser.ui.themeOption.ThemeOptionActivity
import com.example.gitser.ui.userDetail.UserDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchBarSetup()
        showRecycleView()

        userViewModel.listUser.observe(this) { listUser ->
            setUserList(listUser)
        }
        userViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        userViewModel.message.observe(this) { isMessage ->
            showMessage(isMessage)
        }
        userViewModel.errorResponse.observe(this) { errorResponse ->
            showToast(errorResponse)
        }

    }

    private fun searchBarSetup() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.text
                searchView.hide()
                userViewModel.searchUser(searchBar.text.toString())
                false
            }

            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuFavorite -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.menuDarkMode -> {
                        val intent = Intent(this@MainActivity, ThemeOptionActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setUserList(userList: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(userList)
        binding.rvUserList.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
                val intentToDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
                intentToDetail.putExtra(UserDetailActivity.USERNAME, data.login)
                intentToDetail.putExtra(UserDetailActivity.AVATARURL, data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)
    }

    private fun showDetailUser(user: ItemsItem) {
        Toast.makeText(this, "You choose " + user.login, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(isMessage: Boolean) {
        binding.tvUserNotFound.visibility = if (isMessage) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}