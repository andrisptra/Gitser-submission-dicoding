package com.example.gitser.ui.userDetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gitser.R
import com.example.gitser.data.database.FavUserEntity
import com.example.gitser.data.response.DetailUserResponse
import com.example.gitser.databinding.ActivityUserDetailBinding
import com.example.gitser.helper.ViewModelFactory
import com.example.gitser.ui.followFragment.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val AVATARURL = "avatarUrl"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel>() {
        ViewModelFactory.getInstance(application)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = intent.getStringExtra(USERNAME).toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                TAB_TITLES[position]
            )
        }.attach()

        val username = intent.getStringExtra(USERNAME).toString()
        val avatarUrl = intent.getStringExtra(AVATARURL).toString()
        val user = FavUserEntity()
        user.let {
            user.username = username
            user.avatarUrl = avatarUrl
        }

        viewModel.detailUser.observe(this) { detailUser ->
            setUserDetail(detailUser)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.getDetailUser(username)
        viewModel.getUserFavByUsername(username)
            .observe(this) { favUser ->
                if (favUser == null) {
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    binding.btnFavorite.setOnClickListener {
                        viewModel.insert(user)
                        Toast.makeText(
                            this,
                            username + " ditambahkan ke favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    binding.btnFavorite.setOnClickListener {
                        viewModel.delete(user)
                        Toast.makeText(
                            this,
                            username + " dihapus ke favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

    }

    private fun setUserDetail(userName: DetailUserResponse) {
        binding.tvUserName.text = userName.name
        binding.tvName.text = userName.login
        binding.tvFollowing.text = resources.getString(R.string.following, userName.following)
        binding.tvFollower.text = resources.getString(R.string.following, userName.followers)
        Glide.with(binding.root)
            .load(userName.avatarUrl)
            .into(binding.userImage)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
}