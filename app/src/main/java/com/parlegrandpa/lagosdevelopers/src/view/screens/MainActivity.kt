package com.parlegrandpa.lagosdevelopers.src.view.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.parlegrandpa.lagosdevelopers.R
import com.parlegrandpa.lagosdevelopers.src.data.factory.UserItemViewModelFactory
import com.parlegrandpa.lagosdevelopers.src.view.adapter.UserListAdapter
import com.parlegrandpa.lagosdevelopers.src.viewmodel.ListUserViewModel
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.favoriteButton


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListUserViewModel
    private val usersAdapter = UserListAdapter(this, arrayListOf())

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        initView()
    }

    override fun onResume() {
        super.onResume()

        fetchUsers( false)
    }

    fun initView() {
        val modelfactory = UserItemViewModelFactory(application)
        viewModel = ViewModelProvider(this, modelfactory).get(ListUserViewModel::class.java)

        usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }

        favoriteButton.setOnClickListener { startActivity(Intent(this, FavoriteListActivity::class.java)) }

        initSwipeToRefresh()

        observeViewModel()
    }

    fun initSwipeToRefresh() {
        refreshLayout.setOnRefreshListener {
            fetchUsers(true)
        }
    }

    fun fetchUsers(forceLoadFromRemote: Boolean) {
        refreshLayout.isRefreshing = false
        viewModel.refresh(forceLoadFromRemote)
    }

    fun observeViewModel() {
        viewModel.users.observe(this, Observer { users ->
            run {
                users?.let {
                    if (users.isEmpty()) {
                        listError.visibility = View.VISIBLE
                        usersList.visibility = View.GONE
                    } else {
                        usersList.visibility = View.VISIBLE
                        listError.visibility = View.GONE
                        usersAdapter.updateUsers(it) }
                    }
            }
        })

        viewModel.usersLoadError.observe(this, Observer { isError ->
            run {
                isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            run {
                isLoading?.let {
                    loadingView.visibility = if (it) View.VISIBLE else View.GONE
                    if (it) {
                        listError.visibility = View.GONE
                        usersList.visibility = View.GONE
                    }
                }
            }
        })
    }
}