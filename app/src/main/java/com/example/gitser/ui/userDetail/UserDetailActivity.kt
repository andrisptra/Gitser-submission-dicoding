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

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel>() {
        ViewModelFactory.getInstance(application)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username: String = intent.getStringExtra(USERNAME).toString()

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

        viewModel.detailUser.observe(this) { detailUser ->
            setUserDetail(detailUser)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.error.observe(this) { message ->
            showToast(message)
        }
        viewModel.getDetailUser(username)
        viewModel.getUserFavByUsername(username)
            .observe(this) { favUser ->
                isFavUser(favUser)
            }

        toolbarMenu()
        binding.toolbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setUserDetail(userName: DetailUserResponse) {
        binding.tvUserName.text = userName.name
        binding.tvName.text = userName.login
        binding.tvFollowing.text = resources.getString(R.string.following, userName.following)
        binding.tvFollower.text = resources.getString(R.string.follower, userName.followers)
        Glide.with(binding.root)
            .load(userName.avatarUrl)
            .into(binding.userImage)

    }

    private fun isFavUser(favUser: FavUserEntity?) {
        val username: String = intent.getStringExtra(USERNAME).toString()
        val avatarUrl: String = intent.getStringExtra(AVATARURL).toString()
        val user = FavUserEntity()
        user.let {
            user.username = username
            user.avatarUrl = avatarUrl
        }
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

    private fun toolbarMenu() {
        with(binding) {
            toolbar.inflateMenu(R.menu.share_menu)
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuShare -> {
                        val username: String = intent.getStringExtra(USERNAME).toString()
                        val userUrl = "https://github.com/$username"
                        val intent =
                            Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, userUrl)
                        startActivity(Intent.createChooser(intent, "Share User"))
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USERNAME = "username"
        const val AVATARURL = "avatarUrl"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

}